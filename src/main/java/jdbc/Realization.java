package jdbc;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Realization {
    private final static String SETTINGS_PATH = "settings.ini";

    private Properties properties;

    private String dbdzconnectionURL;
    private String dbdzUser;
    private String dbdzPassword;
    private String dbdzDriver;

    private Connection connection;
    private Statement statement;

    //preparedStatements for creating operations
    private PreparedStatement addProjectSt;
    private PreparedStatement addDeveloperSt;
    private PreparedStatement addCustomerSt;

    //preparedStatements for update operations
    private PreparedStatement updateProjectSt;
    private PreparedStatement updateDeveloperSt;
    private PreparedStatement updateCustomerSt;

    //preparedStatements for delete operations
    private PreparedStatement deleteDeveloperSt;
    private PreparedStatement deleteProjectSt;
    private PreparedStatement deleteDeveloperProjectSt;
    private PreparedStatement deleteDeveloperSkillSt;
    private PreparedStatement deleteCompaniesProjectsSt;
    private PreparedStatement deleteCustomersProjectsSt;
    private PreparedStatement deleteCustomerSt;

    public Realization() {
        readSettings();
        initConnection();
        initStatements();
    }

    private void readSettings() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(SETTINGS_PATH));
            dbdzDriver = properties.getProperty("dbdz.driver");
            dbdzconnectionURL = properties.getProperty("dbdz.path");
            dbdzUser = properties.getProperty("dbdz.user");
            dbdzPassword = properties.getProperty("dbdz.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initConnection (){
        try {
            Class.forName(dbdzDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = (Connection) DriverManager.getConnection(dbdzconnectionURL, dbdzUser, dbdzPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initStatements (){
        try {
            statement = (PreparedStatement) connection.createStatement();
            addProjectSt = (PreparedStatement) connection.prepareStatement("insert into projects (ProjectName, description, cost) values (?, ?, ?);");
            addDeveloperSt = (PreparedStatement) connection.prepareStatement("insert into developers (firstName, lastName, age, gender, salary) values (?, ?, ?, ?, ?);");
            addCustomerSt = (PreparedStatement) connection.prepareStatement("insert into customers (CustomerName, StateOrPrivate) values (?, ?);");
            deleteDeveloperSt = (PreparedStatement) connection.prepareStatement("delete from developers where dev_id = ?;");
            deleteDeveloperProjectSt = (PreparedStatement) connection.prepareStatement("delete from developer_projects where project_id =? or dev_id = ?;");
            deleteDeveloperSkillSt = (PreparedStatement) connection.prepareStatement("delete from developer_skill where skill_id = ? or dev_id = ?;");
            deleteProjectSt = (PreparedStatement) connection.prepareStatement("delete from projects where id_project = ?;");
            deleteCompaniesProjectsSt = (PreparedStatement) connection.prepareStatement("delete from companies_projects where company_id = ? or project_id = ?;");
            deleteCustomersProjectsSt = (PreparedStatement) connection.prepareStatement("delete from customers_projects where customer_id = ? or project_id = ?;");
            deleteCustomerSt =(PreparedStatement) connection.prepareStatement("delete from customers where customer_id = ?;");
            updateProjectSt = (PreparedStatement)connection.prepareStatement("update projects set ProjectName = ? , description = ? , cost = ? where project_id = ?;");
            updateDeveloperSt = (PreparedStatement) connection.prepareStatement("update developers set firstName = ? , lastName = ? , age = ?, gender = ?, salary = ? where dev_id = ?;");
            updateCustomerSt = (PreparedStatement) connection.prepareStatement("update customers set CustomerName = ? , StateOrPrivate = ? where customer_id = ?;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createNewTable(String tableName, String sqlColumns) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + sqlColumns + ");";
        try {
            statement.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG INPUT DATA");
        }
    }
    public void getSumOfProjectSalary(int projId) {

        String sql = "SELECT developer_projects.id_project as id_proj, sum(developers.salary) AS SumOfSalary FROM developers, developer_projects " +
                "WHERE developers.dev_id IN ( SELECT DISTINCT developer_projects.dev_id where developer_projects.project_id = " + projId + ");";

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("proj_id");
                int sum = rs.getInt("SumOfSalary");
                System.out.println("project id = " + id + " Sum of salary = " + sum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void getProjectDevelopers(int projNumber) {
        String sql = "SELECT DISTINCT developers.firstName, developers.lastName " +
                "FROM developers, developer_projects " +
                "WHERE  developers.dev_id IN (" +
                "SELECT developer_projects.dev_id " +
                "WHERE (developer_projects.project_id = " + projNumber + "));";
        getDevelopersList(sql);
    }
    public void getJavaDevelopers() {
        String sql = "select DISTINCT developers.firstName, developers.lastName " +
                "from developers, developer_skill " +
                "where  developers.dev_id IN (" +
                "select  developer_skill.dev_id " +
                "where (developer_skill.skill_id in (select skill_id from skills where branch like 'Java' )));";
        getDevelopersList(sql);
    }
    public void getMiddleDevelopers() {
        String sql = "select DISTINCT developers.firstName, developers.lastName " +
                "from developers, developer_skill " +
                "where  developers.dev_id IN (" +
                "select  developer_skill.dev_id " +
                "where (developer_skill.skill_id in (select skill_id from skills where skill_level like 'Middle')));";
        getDevelopersList(sql);
    }
    private void getDevelopersList(String sql) {
        List<String> result = new ArrayList<String>();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getString("firstName") + " " + rs.getString("lastName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        printResult(result);
    }
    public void getProjectsInfo() {
        List<String> result = new ArrayList<>();
        String sql = "select cost, ProjectName, count(developer_projects.dev_id) as DevelopersCount " +
                "from projects, developer_projects " +
                "where projects.project_id = developer_projects.project_id " +
                "group by projects.project_id;";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getInt("cost") + "\t- \t" + rs.getString("ProjectName") +
                        " - " + rs.getInt("DevelopersCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        printResult(result);
    }
    private void printResult(List <String> result){
        for (String singleResult : result
                ) {
            System.out.println(singleResult);
        }
    }
    public void addNewProject(String projectName, String description, int cost) {
        try {
            addProjectSt.setString(1, projectName);
            addProjectSt.setString(2, description);
            addProjectSt.setInt(3, cost);
            addProjectSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewDeveloper(String firstName, String lastName, int age, String gender, long salary) {
        try {
            addDeveloperSt.setString(1, firstName);
            addDeveloperSt.setString(2, lastName);
            addDeveloperSt.setInt(3, age);
            addDeveloperSt.setString(4, gender);
            addDeveloperSt.setLong(5, salary);
            addDeveloperSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addNewCustomer(String customerName, boolean stateOrPrivate) {
        try {
            addCustomerSt.setString(1, customerName);
            addCustomerSt.setBoolean(2, stateOrPrivate);
            addCustomerSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteDeveloper(int id) {
        try {
            deleteDeveloperProjectSt.setInt(1, 0);
            deleteDeveloperProjectSt.setInt(2, id);
            deleteDeveloperSkillSt.setInt(1, 0);
            deleteDeveloperSkillSt.setInt(2, id);
            deleteDeveloperSt.setInt(1, id);
            deleteDeveloperProjectSt.executeUpdate();
            deleteDeveloperSkillSt.executeUpdate();
            deleteDeveloperSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteProject(int id) {
        try {
            deleteDeveloperProjectSt.setInt(1, id);
            deleteDeveloperProjectSt.setInt(2, 0);
            deleteCompaniesProjectsSt.setInt(1, 0);
            deleteCompaniesProjectsSt.setInt(2, id);
            deleteCustomersProjectsSt.setInt(1, 0);
            deleteCustomersProjectsSt.setInt(2, id);
            deleteProjectSt.setInt(1, id);
            deleteDeveloperProjectSt.executeUpdate();
            deleteCompaniesProjectsSt.executeUpdate();
            deleteCustomersProjectsSt.executeUpdate();
            deleteProjectSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCustomer(int id) {
        try {
            deleteCustomersProjectsSt.setInt(1, id);
            deleteCustomersProjectsSt.setInt(2, 0);
            deleteCustomerSt.setInt(1, id);
            deleteCustomersProjectsSt.executeUpdate();
            deleteCustomerSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateProject(String project_id, String projectName, int description, int cost) {
        try {
            updateProjectSt.setString(1, projectName);
            updateProjectSt.setString(2, String.valueOf(description));
            updateProjectSt.setInt(3, cost);
            updateProjectSt.setInt(4, Integer.parseInt(project_id));
            updateProjectSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateCustomer(String customerName, boolean stOrPr, int customer_id) {
        try {
            updateCustomerSt.setString(1, customerName);
            updateCustomerSt.setBoolean(2, stOrPr);
            updateCustomerSt.setInt(3, customer_id);
            updateCustomerSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateDeveloper(String dev_id, String firstName, int lastName, String age, int gender, int salary) {
        try {
            updateDeveloperSt.setString(1, firstName);
            updateDeveloperSt.setString(2, String.valueOf(lastName));
            updateDeveloperSt.setInt(3, Integer.parseInt(age));
            updateDeveloperSt.setString(4, String.valueOf(gender));
            updateDeveloperSt.setInt(5, salary);
            updateDeveloperSt.setInt(6, Integer.parseInt(dev_id));
            updateDeveloperSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection (){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

