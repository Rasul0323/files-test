package pojos;
import java.util.List;

public class LaptopsData {
    private String laptop;
    private String os;
    private List<String> ssd;
    private Ram ram;


    public String getlaptop() {
        return laptop;
    }

    public String getOs() {

        return os;
    }

    public List<String> getSsd() {
        return ssd;
    }

    public Ram getRam() {
        return ram;
    }
}
