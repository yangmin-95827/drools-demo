package org.example.accumulate;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateDataTest {


    /**
     * 商户id
     */
    public static final String merchant="1927,1981,3000,3001,3002,3003,3004,3005,3006,3007,3008,3009,3010,3011,3012,3013,3014,3015,3016,3017,3018,3019,3021,3022,3023,3024,3025" ;

    @Test
    public void create() throws ParseException {
        List<String> sqlLst = new ArrayList<>();
        String marketId= "100000070";
        List<String> dateLst = createDateLst("2021-02-06", 40);
        List<Commodity> commodityLst = getCommodityLst();
        String[] split = merchant.split(",");
        String price = "4.5000";

        Random random = new Random();
        dateLst.stream().forEach(s ->{
            // 随机一天单数
            int i = random.nextInt(30000);
            i += 10000;
            for (int j = 0; j < i; j++) {
                // 商品
                int commodIndex = random.nextInt(commodityLst.size());
                Commodity commodity = commodityLst.get(commodIndex);
                // 商户
                int merchantIndex = random.nextInt(split.length);
                // 重量
                int weight = random.nextInt(4000) + 1000;
                // 金额
                int amount = 45 * weight / 100;
                //
                String sql = "insert into bs_msg_flow_history(device_id,create_time,key_id,product_code,product_name,\n" +
                        "product_unit_price,product_weight,product_amount,merchant_id,market_id,goods_pclass_num,goods_pclass_name,goods_name,\n" +
                        "device_type,bus_type,fqdate,qdate\n" +
                        ")VALUES(8696,SYSDATE(),1,'"+commodity.getSubCategory()+"','"+commodity.getSubCategory()+"'" +
                        ","+price+","+weight+","+amount+","+split[merchantIndex]+","+marketId+","+commodity.getId()+",'"+
                        (commodity.getCategory() +"-"+ commodity.getSubCategory())+"','"+commodity.getSubCategory()+"','1.01',0,"+s+","+s+")";
                sqlLst.add(sql);
            }
        });
        writerSqlFile(sqlLst);

    }

    public List<String> createDateLst(String date,int dayNumber) throws ParseException {
        List<String> lst = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        while (dayNumber -- > 0){
            lst.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
        }
        return lst;
    }


    static class Commodity{
        private String id;
        private String category;
        private String subCategory;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public static Commodity getInstance(String[] com){
            Commodity commodity = new Commodity();
            commodity.setCategory(com[1]);
            commodity.setId(com[0]);
            commodity.setSubCategory(com[2]);
            return commodity;
        }

    }

    public List<Commodity> getCommodityLst(){
        List<Commodity> lst = new ArrayList<>();
        try(InputStream input = this.getClass().getResourceAsStream("../../../data/commodity.txt");
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
        ){
            String line= null;
            while( (line =bufferedReader.readLine()) != null){
                String[] split = line.split(",");
                Arrays.stream(split).forEach(s ->{
                    lst.add(Commodity.getInstance(s.split("|")));
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lst;
    }
    public void writerSqlFile(List<String> sqlLst){
        URL resource = this.getClass().getResource("../../../data/orderFlowHistory.sql");
        try(OutputStream input = new FileOutputStream(new File(resource.getFile()));
            OutputStreamWriter writer = new OutputStreamWriter(input);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
        ){
            sqlLst.forEach(s->{
                try {
                    bufferedWriter.newLine();
                    bufferedWriter.write(s,0,s.length());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
