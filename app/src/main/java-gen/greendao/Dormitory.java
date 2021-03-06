package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DORMITORY.
 */
public class Dormitory {

    private Long dormitory_id;
    private String name;
    private String address;
    private String description;
    private Long company_id;

    public Dormitory() {
    }

    public Dormitory(Long dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    public Dormitory(Long dormitory_id, String name, String address, String description, Long company_id) {
        this.dormitory_id = dormitory_id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.company_id = company_id;
    }

    public Long getDormitory_id() {
        return dormitory_id;
    }

    public void setDormitory_id(Long dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

}
