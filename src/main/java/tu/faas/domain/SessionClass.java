package tu.faas.domain;

import java.util.HashSet;
import java.util.Set;

public class SessionClass {
    private String someName;

    private Set<String> stuff;

    public SessionClass() {
        someName = "Hellou";
        stuff = new HashSet<>();
        stuff.add("stuffs1");
        stuff.add("stuffs2");
    }

    public String getSomeName() {
        return someName;
    }

    public void setSomeName(String someName) {
        this.someName = someName;
    }

    public Set<String> getStuff() {
        return stuff;
    }

    public void setStuff(Set<String> stuff) {
        this.stuff = stuff;
    }
}
