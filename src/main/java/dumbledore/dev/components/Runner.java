package dumbledore.dev.components;

import dumbledore.dev.interfaces.TreeNode;
import dumbledore.dev.interfaces.NodeComparator;

import java.io.File;
import java.time.Instant;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    @Override
    public void run(String[] args) throws Exception {
        logger.info("EXECUTING : Command Line Runner");
        // Define variables
        boolean valid_usage = true;
        boolean verbose = false;
        int depth = 0;
        String phrase = "";

        // Check for valid usage
        if (args.length == 0 || !args[0].equals("analyze") || !args[1].equals("--depth") || args.length < 4 || args.length > 5) {
            logger.info("invalid params?");
            valid_usage = false;
        } else {
            // Assign variables
            try {
                depth = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                logger.info("invalid number");
                valid_usage = false;
            }

            phrase = args[3];

            if (args.length == 5) {
                if (args[4].equals("--verbose")) {
                    verbose = true;
                } else {
                    logger.info("not verbose??");
                    valid_usage = false;
                }
            }
        }

        // Return if invalid usage
        if (!valid_usage) {
            logger.error("Invalid params provided. Example: analyze –depth <n> –verbose (optional) \"{phrase}\"");
            return;
        }

        // Load Json
        Instant start_loadJson = Instant.now();
        List<TreeNode> treeNodeList = loadJson();
        Instant finish_loadJson = Instant.now();
        long loadJson_time = Duration.between(start_loadJson, finish_loadJson).toMillis();

        if (treeNodeList.isEmpty())
            logger.warn("Empty list!");

        // Start processing
        // Get all words in given phrase
        Instant start_processing = Instant.now();
        String[] words = phrase.split("\\s+");
        List<TreeNode> foundNodes = new ArrayList<TreeNode>();
        String output = "0";

        // Find word and parents
        for (String word:words) {
            for (TreeNode node : treeNodeList) {
                if ((node.getTitle()).equalsIgnoreCase(word)) {
                    int found_depth = node.getDepth();
                    String parent = node.getParent();
                    List<TreeNode> parentNodes = new ArrayList<TreeNode>();
                    while (found_depth > 1) {
                        logger.info(String.valueOf(found_depth));
                        for (TreeNode tn : treeNodeList) {
                            if ((tn.getTitle()).equalsIgnoreCase(parent)){
                                parent = tn.getParent();
                                parentNodes.add(tn);
                                if (parent == null) {
                                    break;
                                }
                            }
                        }
                        found_depth--;
                    }
                    parentNodes.sort(new NodeComparator());
                    node.setParentNodes(parentNodes);
                    foundNodes.add(node);
                }
            }
        }

        // Create result
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (TreeNode foundNode:foundNodes) {
            if (foundNode.getDepth() == depth) {
                int count = 1;
                if (map.containsKey(foundNode.getTitle())) {
                    count = map.get(foundNode.getTitle()) + 1;
                }
                map.put(foundNode.getTitle(), count);
            }
            if (foundNode.getParent() != null) {
                for (TreeNode parentNode:foundNode.getParentNodes()) {
                    if (parentNode.getDepth() == depth) {
                        int count = 1;
                        if (map.containsKey(parentNode.getTitle())) {
                            count = map.get(parentNode.getTitle()) + 1;
                        }
                        map.put(parentNode.getTitle(), count);
                    }
                }
            }
        }

        // Print result
        map.forEach((key, value) -> {
            System.out.println(key + " = " + value + ";");
        });

        Instant finish_processing = Instant.now();
        long processing_time = Duration.between(start_processing, finish_processing).toMillis();

        if (verbose) {
            String[][] table = {
                {"Tempo de carregamento dos parâmetros ", String.valueOf(loadJson_time) + "ms"},
                {"Tempo de verificação da frase        ", String.valueOf(processing_time) + "ms"}
            };

            System.out.println(drawTable(table));
        }
    }

    private List<TreeNode> loadJson() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            treeNodeList = objectMapper.readValue(new File("./dicts/dictionary.json"), new TypeReference<List<TreeNode>>(){});
        } catch (java.io.IOException e) {
            logger.error(e.getMessage());
        }

        return treeNodeList;
    }

    public static String drawTable(String[][] table) {
        String borderRow = Arrays.stream(table[0])
                // border row between rows
                .map(str -> "-".repeat(str.length()))
                .collect(Collectors.joining("+", "+", "+\n"));
        return Arrays.stream(table)
                // table row with borders between cells
                .map(row -> Arrays.stream(row)
                        .collect(Collectors.joining("|", "|", "|\n")))
                .collect(Collectors.joining(borderRow, borderRow, borderRow));
    }
}