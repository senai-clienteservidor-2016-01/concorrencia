/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package concorrencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francisco
 */
public class Concorrencia {
    
    private static final int qtd = 2;
    
    
    private static Connection conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql:estoque", "postgres", "123456");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Concorrencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Concorrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static int getProximoId(Connection con, int cupomId) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(
                "select nr_item from item_cupom where cupom_id = ? for update",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        
        stmt.setInt(1, cupomId);
        stmt.executeQuery();
        
        PreparedStatement pstmt = con.prepareStatement
                ("select max(nr_item) from item_cupom where cupom_id = ?");
        pstmt.setInt(1, cupomId);
        
        ResultSet rs = pstmt.executeQuery();
        try{
            if(rs.next()){
                return rs.getInt(1) + 1;
            }else{
                return 1;
            }
        }finally{
//            rs.close();
//            stmt.close();
//            pstmt.close();
        }
    }
    
    public static void inserirItem(Connection con, int cupomId, boolean demorar) throws SQLException, InterruptedException{
        con.setAutoCommit(false);
        int nrItem = getProximoId(con, cupomId);
        if(demorar){
            Thread.sleep(200);
        }
        PreparedStatement pstmt = con.prepareStatement("insert into item_cupom(cupom_id, nr_item) values(?, ?);");
        pstmt.setInt(1, cupomId);
        pstmt.setInt(2, nrItem);
        pstmt.execute();
        pstmt.close();
        con.commit();
    }
    
    private static boolean seraQueVaiDemorar(){
        double valor = Math.random();
        return valor > 0.8;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        for (int i = 0; i < qtd; i++) {
            new Thread(){
                @Override
                public void run() {
                    Connection con = conectar();
                    for(int i = 0; i < 300; i++){
                        try {
                            inserirItem(con, 17, seraQueVaiDemorar());
                        } catch (Exception ex) {
                            Logger.getLogger(Concorrencia.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            }.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Concorrencia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
