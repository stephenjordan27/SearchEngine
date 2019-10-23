
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;

class OurPipeline {

    private static Properties properties;
    private static String propertiesName = "tokenize, ssplit, pos, lemma";
    private static StanfordCoreNLP stanfordCoreNLP;

    private OurPipeline() {
    }

    public static StanfordCoreNLP getPipeline() {
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}
