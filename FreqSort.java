import java.util.Scanner;
class FreqSort{
    
    public static final int MAX = 254;
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Size of array");
        int n = sc.nextInt();
        int[][] ar = new int[MAX][2];
        System.out.println("Enter elements of an array");
        int k = 0, temp, count, flag = 0;
        for(int i = 0; i< n; i++){
            ar[i][0] = sc.nextInt();
            ar[i][1] = 0;
        }
        
        int[][] br = new int[MAX][2];
        for(int i = 0; i< n; i++){
            if(ar[i][1] == 1)
                continue;
            count = 1;
            for(int j = i +1 ; j < n; j++){
                if(ar[i][0] == ar[j][0]){
                    ar[j][1] = 1;
                    count++;
                }
            }            
            br[k][0]= ar[i][0];
            br[k][1] = count;
            k++;
        }
        
        n = k;
        for(int i = 0; i< n-1; i++){
            temp = br[i][1];
            for(int j = i+1; j< n; j++){
                if(temp > br[j][1]){
                    temp = br[j][1];
                    br[j][1] = br[i][1];
                    br[i][1] = temp;
                    
                    temp = br[j][0];
                    br[j][0] = br[i][0];
                    br[i][0] = temp;
                }
            }
        }
        
        for(int i = 0; i< n; i++){
            while(br[i][1] != 0){
                if(flag == 0){
                    System.out.print(3+" ");
                    flag = 1;
                }
                System.out.print(br[i][0]+" ");
                br[i][1]--;
            }
        }
    }
}