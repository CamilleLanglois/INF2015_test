/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inf;
import net.sf.json.*;
/**
 *
 * @author Utilisateur
 */
public class Validation {
    
public static void hasEmploye(JSONArray employes) throws Exception {
    
    if (employes.isEmpty()) {
        throw new Exception ("Le département doit avoir au moins un employé.");
    }
    
}
    
}
