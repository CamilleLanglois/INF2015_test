/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inf;

import static com.inf.Employe.finalEmployeList;
import java.util.ArrayList;

/**
 *
 * @author camillelanglois
 */
public class Recommandation {
    public static ArrayList<Recommandation> recommandationList = new ArrayList<Recommandation>();
    
    private String message;
    
    public Recommandation(String message){
        this.message = message;
        addRecommandationToList();
    }
    
       private void addRecommandationToList(){
        recommandationList.add(this);
    }
}
