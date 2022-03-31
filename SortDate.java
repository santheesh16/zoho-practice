import java.util.Scanner;

class SortDate{
    
    static int getCount(String[] a, int div, int mod, range){
        
    }
    
    static void sortDates(int a[], n){
        getCount(a, 1000000, 100, 32);
        getCount(a, 1000, 100, 32);
    }
    
     public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter no of dates");
        int n = sc.nextInt();
        String a[] = new String[n];
        for(int i = 0; i< n; i++){
            a[i]= sc.nextLine();
        }
        sortDates(a, n);
        
    }
}