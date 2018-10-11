package team.alpha.model;

public class UserPreferences {
    String city;
    String country;
    String company;
    boolean subscribedToNewsAlerts;
    boolean subscribedToWeatherAlerts;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isSubscribedToNewsAlerts() {
        return subscribedToNewsAlerts;
    }

    public void setSubscribedToNewsAlerts(boolean subscribedToNewsAlerts) {
        this.subscribedToNewsAlerts = subscribedToNewsAlerts;
    }

    public boolean isSubscribedToWeatherAlerts() {
        return subscribedToWeatherAlerts;
    }

    public void setSubscribedToWeatherAlerts(boolean subscribedToWeatherAlerts) {
        this.subscribedToWeatherAlerts = subscribedToWeatherAlerts;
    }
}
