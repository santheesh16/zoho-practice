import java.util.Scanner;

class FindRising{

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Size of array");
        int n = sc.nextInt();
        int a[] = new int[n];
        System.out.println("Enter elements of an array");
        for(int i = 0; i< n; i++){
            a[i] = sc.nextInt();
        }
        if(a[0] <= a[1] && a[n -2] <= a[n -1]){
            System.out.println("Increasing");
        }else if(a[0] >= a[1] && a[n -2] >= a[n -1]){
            System.out.println("Decreasing");
        }else if(a[0] <= a[1] && a[n -2] >= a[n -1]){
            System.out.println("Increasing Decreasing");
        }else{
            System.out.println("Decreasing Increasing");
        }
        
    }
}