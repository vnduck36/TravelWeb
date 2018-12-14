package bsn_traveladvisor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 *
 * @author DudhatB1581
 */
public class CommonMethods {

    public static boolean isInteger(String a) {
        try {
            int i = Integer.parseInt(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String acceptTags() {
        String[] t = {"History Buff", "Shopping Fanatic", "Beach Goer", "Urban Explorer", "Nature Lover", "Family Vacationer"};
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        String tags = "";
        int nooftags = 0;
        String more = "";

        do {
            System.out.println("Select the tags");
            for (int i = 0; i < t.length; i++) {
                System.out.println(i + 1 + "." + t[i]);
            }
            System.out.println("Enter tag choice [1-6]");
            String sel = sc.nextLine();
            if (CommonMethods.isInteger(sel)) {
                choice = Integer.parseInt(sel);
            } else {
                System.out.println("Please select correct tag number");
                continue;
            }

            if (choice < 1 || choice > t.length) {
                System.out.println("Please select correct tag number");
                continue;
            }
            tags = tags + t[choice - 1] + "#";
            nooftags++;
            System.out.println("Want to add more tags? Y to add more , other key to continue");
            more = sc.nextLine();
        } while ((more.equalsIgnoreCase("Y") || nooftags == 0) && nooftags < t.length);

        System.out.println(tags);
        return tags;
    }
}
