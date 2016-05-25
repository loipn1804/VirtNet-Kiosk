package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(3, "greendao");

        Entity dormitory = schema.addEntity("Dormitory");
        dormitory.addLongProperty("dormitory_id").primaryKey();
        dormitory.addStringProperty("name");
        dormitory.addStringProperty("address");
        dormitory.addStringProperty("description");
        dormitory.addLongProperty("company_id");

        Entity category = schema.addEntity("Category");
        category.addLongProperty("category_id").primaryKey();
        category.addStringProperty("name");
        category.addStringProperty("description");

        Entity country = schema.addEntity("Country");
        country.addLongProperty("order");
        country.addStringProperty("code").primaryKey();
        country.addStringProperty("slug");
        country.addStringProperty("name");

        Entity validate = schema.addEntity("Validate");
        validate.addLongProperty("worker_id").primaryKey();
        validate.addIntProperty("count");
        validate.addLongProperty("time");

        new DaoGenerator().generateAll(schema, args[0]);
    }

}
