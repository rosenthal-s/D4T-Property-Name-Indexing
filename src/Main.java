import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
//    private static Map<String, String> readCodeFile()
//    {
//        Map<String, String> codeMap = new HashMap<>();
//
//        try {
////            BufferedReader fr = new BufferedReader( new FileReader( "TTIcode GIATA Xref.csv" ) );
//            BufferedReader br = new BufferedReader( new FileReader( "TTI Giata Xrefs 20Feb24 file 1.csv" ) );
//            String line;
//
//            br.readLine(); // The first line is just the column headings of the CSV
//
//            while( ( line = br.readLine() ) != null )
//            {
////                String[] codes = line.split( "," );
//                String[] codes = line.split( ";" );
//                codeMap.put( codes[0], codes[1] );
//
////                System.out.println( codeMap.get( codes[0] ) ); ///
//            }
//
//            br.close();
//
//
//
//            br = new BufferedReader( new FileReader( "TTI Giata Xrefs 20Feb24 file 2.csv" ) );
//            br.readLine(); // The first line is just the column headings of the CSV
//
//            while( ( line = br.readLine() ) != null )
//            {
//                String[] codes = line.split( ";" );
//                codeMap.put( codes[0], codes[1] );
//
////                System.out.println( codeMap.get( codes[0] ) ); ///
//            }
//
//            br.close();
//        }
//        catch( FileNotFoundException e )
//        {
//            System.err.println( "Unable to open TTIcode Giata Xref file" );
//            System.exit( 1 );
//        }
//        catch( IOException e )
//        {
//            System.err.println( "Error reading TTIcode Giata Xref file" );
//            System.exit( 1 );
//        }
//
//        return codeMap;
//    }



    private static ArrayList<String> readFile( String filePath )
    {
        ArrayList<String> entries = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader( new FileReader( filePath ) );
            String line;

            br.readLine(); // The first line is just the column headings of the CSV:
            // TTICODE;NAME;CITY;LOCALE;COUNTRY;ADDRESS;STREETNMBR;ADDRESSLINE;POSTALCODE;CITYNAME;PHONE;FAX;EMAIL;URL;LATITUDE;LONGITUDE;ACCURACY;CHAINS;DEFAULT_RATING;PRIMARY_PROPERTY_TYPE;ALTERNATIVE_NAMES;PARENT_TTICODE;AIRPORTS
            ///   0     1     2     3       4       5       6           7           8       9       10    11  12   13     14        15      16       17        18                 19                    20             21          22

            // Each entry consists of the following fields: TTIcode[0], Giata Id (from 2nd file), Property or Alternate Name[1 and 20], Star Rating[18?],
            // City[2] / Resort Name[???], Resort Area/State/Locale Name[3], Country Name[4], City / Resort Internal Id (when available) [???],
            // Resort Area / State / Locale Id (when available) [???], ISO Country Code (when available) [???], P/A tag

            while( ( line = br.readLine() ) != null )
            {
                // Some entries are wrapped by speech marks for some reason - these need to be removed
                if( line.equals("")) /// Empty lines cause issues with the next check, if not also with the table as a whole
                {
                    continue;
                }

                if( line.charAt( 0 ) == '"' ) /// This issue may be resolved?
                {
                    line = line.substring( 1, line.length()-1 );
                }

                entries.add( line );
//                String[] blocks = line.split(";"); /// Do in body?

//                System.out.println("test"); ///
            }

            br.close();
        }
        catch( FileNotFoundException e )
        {
            System.err.println( "Unable to open file '" + filePath + "'" );
            System.exit( 1 );
        }
        catch( IOException e )
        {
            System.err.println( "Error reading file '" + filePath + "'" );
            System.exit( 1 );
        }

        return entries;
    }



    private static void writeToFile( String filePath, ArrayList<String> indices )
    {
        try
        {
            BufferedWriter bw = new BufferedWriter( new FileWriter( filePath ) );

            bw.write( "sep=;" ); // Specify the delimiter of the CSV file to be ";" /// Messes up anything but Excel?
            bw.newLine();

            /// TTIcode[0], Giata Id (from 2nd file), Property or Alternate Name[1 and 20], Star Rating[18?],
            /// City / Resort Name[2], Resort Area/State/Locale Name[3], Country Name[4], City / Resort Internal Id (when available) [???],
            /// Resort Area / State / Locale Id (when available) [???], ISO Country Code (when available) [???], P/A tag
//            bw.write( "TTICODE;GIATA ID;PROPERTY OR ALTERNATE NAME;STAR RATING;CITY / RESORT NAME;RESORT AREA / STATE / LOCALE NAME;COUNTRY NAME;PRIMARY OR ALTERNATE NAME" );
            bw.write( "TTICODE;GIATA ID;PROPERTY OR ALTERNATE NAME;STAR RATING;CITY / RESORT NAME;CITY / RESORT INTERNAL ID;RESORT AREA / STATE / LOCALE NAME;RESORT AREA / STATE / LOCALE ID;COUNTRY NAME;COUNTRY CODE;PRIMARY OR ALTERNATE NAME" );
            bw.newLine();

            for( String index : indices )
            {
                bw.write( index );
                bw.newLine();
            }

            bw.flush();
            bw.close();
        }
        catch( IOException e )
        {
            System.err.println( "Error writing to file '" + filePath + "'" );
            System.exit( 1 );
        }
    }



    public static String formatIndex( String ttiCode, String giataID, String propertyName, String starRating, String cityName, String cityID, String localeName, String localeID, String countryName, String countryCode, String pOrATag )
    {
        return ttiCode + ";" + // TTIcode
               giataID + ";" + // Giata ID
               propertyName + ";" + // Property Name
               starRating + ";" + // Star Rating
               cityName + ";" + // City / Resort Name
               cityID + ";" + /// City / Resort Internal ID
               localeName + ";" + // Resort Area / State / Locale Name
               localeID + ";" +/// Resort Area / State / Locale ID
               countryName + ";" + // Country Name
               countryCode + ";" + /// 2 Character ISO Country Code
               pOrATag; // Primary vs. Alternate Name Tag
    }



    public static void main( String[] args )
    {
        Scanner sc = new Scanner( System.in );

//        Map<String, String> codeMap = readCodeFile();

        System.out.print( "Enter the input file name or path:\n" );
        String inputFilePath = sc.nextLine();

        ArrayList<String> entries = readFile( inputFilePath );



        // TTICODE;NAME;CITY;LOCALE;COUNTRY;ADDRESS;STREETNMBR;ADDRESSLINE;POSTALCODE;CITYNAME;PHONE;FAX;EMAIL;URL;LATITUDE;LONGITUDE;ACCURACY;CHAINS;DEFAULT_RATING;PRIMARY_PROPERTY_TYPE;ALTERNATIVE_NAMES;PARENT_TTICODE;AIRPORTS
        ///   0     1     2     3       4       5       6           7           8       9       10    11  12   13     14        15      16       17        18                 19                    20             21          22

        // Each entry consists of the following fields: TTIcode[0], Giata Id (from 2nd file), Property or Alternate Name[1 and 20], Star Rating[18?],
        // City / Resort Name[2], Resort Area/State/Locale Name[3], Country Name[4], City / Resort Internal Id (when available) [???],
        // Resort Area / State / Locale Id (when available) [???], ISO Country Code (when available) [???], P/A tag
        ArrayList<String> indices = new ArrayList<>();

        for( String entry : entries )
        {
            String[] blocks = entry.split( ";", -1 );

            // In case any empty lines get read as an entry, ignore
            if( blocks.length == 1 )
            {
                continue;
            }

            if( blocks.length < 5 ) ///
            {
                System.out.println("Error"); ///
                continue;
            }

            String ttiCode = blocks[0];
//            String giataID = codeMap.get( blocks[0] );
            String giataID = blocks[1]; ///
            String starRating = blocks[3];
            String cityName = blocks[4];
            String cityID = blocks[5];
            String localeName = blocks[6];
            String localeID = blocks[7];
            String countryName = blocks[8];
            String countryCode = blocks[9];

            // Add an entry for the primary name
//            blocks[0] = blocks[0].replace( "\"", "" ); /// Temp
            indices.add( formatIndex( ttiCode, giataID, blocks[2], starRating, cityName, cityID, localeName, localeID, countryName, countryCode, "P" ) );

            // Add entries for each alternate name
            for( int i = 11; i < blocks.length; i++ ) /// Set to i=10 if ALT FLAG is removed, otherwise a name will be missed
            {
                if( !blocks[i].isEmpty() )
//                if( !blocks[i].isEmpty() && !blocks[i].equals("P") && !blocks[i].equals("A") )
                {
                    indices.add( formatIndex( ttiCode, giataID, blocks[i], starRating, cityName, cityID, localeName, localeID, countryName, countryCode, "A" ) );
                }
            }
        }



        System.out.print( "\n\nEnter the output file name or path:\n" );
        String outputFilePath = sc.nextLine();

        writeToFile( outputFilePath, indices );
    }
}