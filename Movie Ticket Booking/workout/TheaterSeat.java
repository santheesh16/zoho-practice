import java.util.Arrays;
import java.util.Scanner;

class TheaterSeat{
    
    static String extractInt(String str)
    {
        str = str.replaceAll("[^\\d]", " ");
        str = str.trim();
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
        return str;
    }
    
    
    public static void main(String[] args){
        
        int m = 100;
        m = m/10 ;
        String[][] str = new String[m][10];
        
        for(int i = 0, c = 'A'; i < m ;i++, c++){
            for(int j = 0,p=1; j < 10; j++, p++){
                str[i][j] = ((char)c) + String.valueOf(p);
            }
        }
        for(String[] s : str){
            System.out.println(Arrays.toString(s));
        }
        
        
        System.out.println("Select your seats Eg.A1-A10");
		Scanner sc = new Scanner(System.in);
		String bookedSeats = sc.nextLine();
        int rowStart, rowEnd,colEnd = 0,colStart = 0, colS = 0,colE = 0;
        if(bookedSeats.length() > 2){
            String[] arr = extractInt(bookedSeats).split(" ");
            System.out.println(Arrays.toString(arr));
            rowStart = ((int)bookedSeats.charAt(0)) - 65;
            rowEnd  =  ((int)bookedSeats.charAt(3)) - 65;
            colE = Integer.parseInt(arr[1]);
            colS = Integer.parseInt(arr[0]) - 1;
        }else{
            String[] arr = extractInt(bookedSeats).split(" ");
            rowStart = ((int)bookedSeats.charAt(0)) - 65;
            rowEnd  =  rowStart ;
            colS = Integer.parseInt(arr[0]);
            colE = Integer.parseInt(arr[0]) + 1;
        }
        int userId = 1;
			
		for(int i = 0, c = 'A'; i < m ;i++, c++){
            for(int j = 0,p=1; j < 10; j++, p++){
                if(bookedSeats.length() > 2){
                    if(rowEnd == i){
                        colEnd = colE;
                    }else{
                        colEnd = 10;
                    }
                    if(i == rowStart){
                        colStart = colS;
                    }else{
                        colStart = 0;
                    }
                }else{
                    colStart = colS;
                    colEnd = colE;
                }
				if(rowStart<= i && rowEnd >= i &&  colStart <= j && colEnd > j ){
					str[i][j] = String.valueOf(1);
				}else{
					str[i][j] = ((char)c) + String.valueOf(p);
				}
            }
        }
        for(String[] s : str){
            System.out.println(Arrays.toString(s));
        }
        int bookedTic , avail ;
        bookedTic = avail = 0;
		for(int i = 0; i < str.length ;i++){
            for(int j = 0; j < 10; j++){
                if(str[i][j].equals("1")){
                    bookedTic++;
                }else{
                    avail++;
                }
            }
        }
		System.out.printf("Booked: %d\t Availble: %d\n",bookedTic,avail);
        
        for(int i = 0, c = 'A'; i < m ;i++, c++){
            for(int j = 0,p=1; j < 10; j++, p++){
                if((Character.isDigit(str[i][j].charAt(0)) && (str[i][j]).equals("1"))){
                    
                    System.out.printf(str[i][j]);
                }else{
                        str[i][j] = ((char)c) + String.valueOf(p);
                }
                    
            }
        }
         for(String[] s : str){
            System.out.println(Arrays.toString(s));
        }
    }
}