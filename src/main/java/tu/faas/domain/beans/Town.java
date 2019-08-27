package tu.faas.domain.beans;

public class Town {
    private Integer population;
    private String country;

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Town{" +
                "population=" + population +
                ", country='" + country + '\'' +
                '}';
    }
}
