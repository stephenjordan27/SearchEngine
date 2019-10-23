import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 *
 * @author asus a455l
 */
class Normalization 
{
    public static String nfcNormalized(String txt) 
    {
        if(!Normalizer.isNormalized(txt, Normalizer.Form.NFKC)) 
        {
            return Normalizer.normalize(txt, Normalizer.Form.NFKC);
        }
        return txt;
    }
    
    public static String formatString(String s) 
    {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFKD);
        temp = temp.replaceAll("[^\\p{ASCII}]", "");
        return temp;
    }
}
