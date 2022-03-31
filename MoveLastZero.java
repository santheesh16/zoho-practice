import java.util.Scanner;

class MoveLastZero{
    
    static int[] zeroLast(int a[], int n){
        int count = 0;
         for(int i =0; i< n; i++){
             if(a[i] != 0){
                 a[count] = a[i];
                 count++;
             }
         }
         for(int i = count; i <n ; i++){
             a[i] = 0;
         }
        return a;
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Size of array");
        int n = sc.nextInt();
        int a[] = new int[n];
        System.out.println("Enter elements of an array");
        for(int i = 0; i< n; i++){
            a[i] = sc.nextInt();
        }
        int zerolastArr[] = zeroLast(a,n);
        for(int i = 0; i< n; i++){
            System.out.printf("%d ", zerolastArr[i]);
        }
    }
}