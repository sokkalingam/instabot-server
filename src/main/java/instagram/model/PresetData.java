package instagram.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PresetData {

    @Id
    private String _id;

    @NotEmpty(message = "Preset Name cannot be empty")
    private String name;

    @NotNull(message = "Data cannot be null")
    private Data data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PresetData that = (PresetData) o;
        return Objects.equals(name, that.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }

    @Override public String toString() {
        return "PresetData{" + "presetName='" + name + '\'' + ", data=" + data + '}';
    }
}
