package controller;

import service.EreignisService;
import service.FahrerService;
import service.StrafeService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleController {

    private final StrafeService strafeService;
    private final EreignisService ereignisService;
    private final FahrerService fahrerService;

    private final Scanner scanner = new Scanner(System.in);

    // folosim LinkedHashMap ca să păstrăm ordinea meniului
    private final Map<Integer, MenuItem> menu = new LinkedHashMap<>();

    public ConsoleController(StrafeService StrafeService,
                             EreignisService ereignisService,
                             FahrerService FahrerService) {
        this.strafeService = StrafeService;
        this.ereignisService = ereignisService;
        this.fahrerService = FahrerService;

        registerMenu();
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readInt("Choose option: ");

            if (choice == 0) {
                System.out.println("Bye!");
                return;
            }

            MenuItem item = menu.get(choice);
            if (item == null) {
                System.out.println("Invalid option.");
                continue;
            }

            System.out.println("--------------------------------------------------------------");
            try {
                item.action.run();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                // debug:
                // e.printStackTrace();
            }
            System.out.println("--------------------------------------------------------------");
        }
    }

    // -------------------- MENU REGISTRATION --------------------

    private void registerMenu() {

        // 1
        menu.put(1, new MenuItem("Load & print counts + all drivers", this::option1));

        // 2
        menu.put(2, new MenuItem("Filter active drivers by team", this::option2));

        // 3
        menu.put(3, new MenuItem("Sort drivers: skill desc, name asc", this::option3));

        // 4
        menu.put(4, new MenuItem("Write sorted drivers to data/drivers_sorted.txt", this::option4));

        // 5
        menu.put(5, new MenuItem("Compute points for first 5 events", this::option5));

        // 6
        menu.put(6, new MenuItem("Ranking Top 5 drivers by total score", this::option6));

        // 7
        menu.put(7, new MenuItem("Generate data/race_report.txt", this::option7));
    }

    private void printMenu() {
        System.out.println();
        System.out.println("============== FORMULA 1 CONSOLE ==============");
        menu.forEach((k, v) -> System.out.printf("%d. %s%n", k, v.label));
        System.out.println("0. Exit");
        System.out.println("==================================================");
    }

    // -------------------- OPTIONS (1..7) --------------------

    /**
     * 1) Load data + print:
     * - counts drivers, events, penalties
     * - all drivers (one per line)
     */
    private void option1() {

        fahrerService.printDriversCount();
        ereignisService.printEventsCount();
        strafeService.printPenaltiesCount();

        fahrerService.printAllFahrers().forEach(System.out::println);
    }

    /**
     * 2) Filter active drivers by team (read team)
     */
    private void option2() {
        String team = readNonEmpty("Input name: ");
        fahrerService.filterActiveFahrersByTeam(team)
                .forEach(System.out::println);
    }

    /**
     * 3) Sort tributes by skill desc then name asc
     */
    private void option3() {
        fahrerService.sortBySkillDescThenNameAsc()
                .forEach(System.out::println);
    }

    /**
     * 4) Save sorted tributes to file
     */
    private void option4() {
        fahrerService.saveSortedFahrersToFile();
        System.out.println("Saved to data/drivers_sorted.txt");
    }

    /**
     * 5) Compute points for first 5 events and print each line
     */
    private void option5() {
        ereignisService.calculateAndPrintEventPoints()
                .forEach(System.out::println);
    }
//
//    /**
//     * 6) Ranking Top 5
//     */
    private void option6() {
        fahrerService.printTop5FahrerRanking();
    }
//
//    /**
//     * 7) Arena report file
//     */
    private void option7() {
        ereignisService.generateRaceReport();
        System.out.println("Generated data/race_report.txt");
    }

    // -------------------- INPUT HELPERS --------------------

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }


    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Value must not be empty.");
        }
    }

    // -------------------- SMALL MENU ITEM MODEL --------------------

    private static class MenuItem {
        final String label;
        final Runnable action;

        MenuItem(String label, Runnable action) {
            this.label = label;
            this.action = action;
        }
    }
}
