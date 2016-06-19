/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresDAO;

import java.util.ArrayList;

import Entities.ChartData;
import Utils.ConfigManager;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetChartData implements Serializable {

    private String driver = "";
    private String protocol = "";
    private String usrname = "";
    private String password = "";

    public String GenerateJSonChartData(String jsonp, String kind) throws JSONException, SQLException {

        String chartType = jsonp;
        List<String> result = new ArrayList();
        String kindOfReport = "Barios";
        if (kind.equals("1")) {
            kindOfReport = "Categorias";
            result = top10CatRanking();
        } else {
            result = top10BarrioRanking();
        }

        JSONObject finalJSonObj = new JSONObject();

        ChartData cd;

        ArrayList<ChartData> chartDataArray = new ArrayList<ChartData>();

        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                String[] l = result.get(i).split(";");
                cd = new ChartData();
                cd.setParametro(l[0]);
                cd.setCantidad(Integer.parseInt(l[1]));
                chartDataArray.add(cd);
            }

        }

        JSONArray chartData = new JSONArray();
        JSONArray xaxisArr = new JSONArray();

        JSONObject xaxisObj = new JSONObject();

        JSONObject dataObj = new JSONObject();

        JSONArray cantidad = new JSONArray();

        for (int i = 0; i < chartDataArray.size(); i++) {

            System.out.println("Json data " + i);

            xaxisArr.put(chartDataArray.get(i).getParametro());

            cantidad.put(chartDataArray.get(i).getCantidad());

        }

        xaxisObj.put("category", xaxisArr);

        chartData.put(xaxisObj);
        dataObj = new JSONObject();
        dataObj.put("name", kindOfReport);
        dataObj.put("color", "##5cb85c");
        dataObj.put("data", cantidad);
        chartData.put(dataObj);

        System.out.println("Json data " + chartData);

        finalJSonObj.put(chartType, chartData);

        String tempStr = jsonp + "(" + finalJSonObj.toString() + ")";

        return tempStr;
    }

    List top10CatRanking() throws SQLException {
        List result = new ArrayList();
        Connection con = null;
        Statement s = null;
        ResultSet rs = null;

        try {

            ConfigManager configuracion = new ConfigManager();
            Properties propiedades = configuracion.getConfigFile("Config.properties");
            driver = propiedades.getProperty("postgress-driver");
            protocol = propiedades.getProperty("jdbc-url");
            usrname = propiedades.getProperty("tsig-usr");
            password = propiedades.getProperty("tsig-pwd");
            String cc = protocol + "?"
                    + "user=" + usrname + "&password=" + password;
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(cc);
            s = con.createStatement();
            String query = "select u.nombre , count(u.id) as cantidad from ( "
                     +"SELECT c.nombre as nombre, po.id as id  "
                     +"  FROM categoria c ,punto_ombu po  "
                     +"  WHERE c.id=po.id_categoria "
                     +"UNION "
                     +"SELECT c2.nombre as nombre, ro.id as id  "
                     +" FROM categoria c2 ,referencia_ombu ro  "
                     +" WHERE c2.id=ro.categoria_id "
                     +") as u "
                     +"GROUP BY u.nombre ORDER BY cantidad desc  LIMIT 10";
            rs = s.executeQuery(query);
            while (rs.next()) {
                String category = rs.getString(1);
                String count = rs.getString(2);
                String toAdd = category + ";" + count;
                result.add(toAdd);
            }

        } catch (Exception e) {
            System.out.println("Error en ReportsSQLController;top10CatRanking " + e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (s != null) {
                    s.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (Exception e2) {
                System.out.println("Error en ReportsSQLController;top10CatRanking;closeConnection " + e2.toString());
            }
        }

        return result;
    }

    List top10BarrioRanking() throws SQLException {
        List result = new ArrayList();
        Connection con = null;
        Statement s = null;
        ResultSet rs = null;

        try {

            ConfigManager configuracion = new ConfigManager();
            Properties propiedades = configuracion.getConfigFile("Config.properties");
            driver = propiedades.getProperty("postgress-driver");
            protocol = propiedades.getProperty("jdbc-url");
            usrname = propiedades.getProperty("tsig-usr");
            password = propiedades.getProperty("tsig-pwd");
//              String cc=protocol+"?" +
//                 "user="+usrname+"&password="+password;
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(protocol, usrname, password);
            s = con.createStatement();
            String query = "SELECT res.barrio, COUNT(res.ombu_id) as cantidad FROM "
                    + "(SELECT DISTINCT b.barrio as barrio ,po.ombu_id as ombu_id "
                    + "FROM "
                    + //                        "ombues o, categoria c, \n" +
                    "barrios b, punto_ombu po WHERE st_contains(st_transform(st_setsrid(b.geom,32721),3857), po.geom) ) as res "
                    + "GROUP BY res.barrio ORDER BY cantidad desc "
                    + "LIMIT 10";
            rs = s.executeQuery(query);
            while (rs.next()) {
                String barrio = rs.getString(1);
                String count = rs.getString(2);
                String toAdd = barrio + ";" + count;
                result.add(toAdd);
            }

        } catch (Exception e) {
            System.out.println("Error en ReportsSQLController;top10CatRanking " + e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (s != null) {
                    s.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (Exception e2) {
                System.out.println("Error en ReportsSQLController;top10CatRanking;closeConnection " + e2.toString());
            }
        }

        return result;
    }

}
