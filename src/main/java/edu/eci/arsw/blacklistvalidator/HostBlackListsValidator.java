/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.BlackListThread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator{
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public String checkHost(int numberThreads, String ipaddress){
        String blackListOcurrences="";
        int ocurrencesCount=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        ArrayList<BlackListThread> threadList = createThreadList(numberThreads,ipaddress,skds);
        for (int i=0;i<numberThreads && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++) {
            threadList.get(i).start();
            ocurrencesCount+= threadList.get(i).getBlackListOcurrences().size() > 0 ? 1 : 0;
            blackListOcurrences = threadList.get(i).getBlackListOcurrences().stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
        for (BlackListThread t : threadList){
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{threadList, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }

    /**
     * Esta funcion permite retornar una lista de valores donde una ip fue encontrada en las black list
     * @param list
     * @return una lista con los servers donde se encuentra la ip
     */
    public ArrayList<Integer> serversFounded(ArrayList<BlackListThread> list, String ipaddress){
        ArrayList<Integer> serversFounded = new ArrayList<>();
        for (BlackListThread thread : list) {
            if (thread.getBlackListOcurrences().size()>0) {
                for (Integer number : thread.getBlackListOcurrences()){
                    serversFounded.add(number);
                }
            }
        }
        return serversFounded;
    }

    /**
     * Permite dividir la busqueda de la ip en las blacklist, en n hilos
     * @param numberThreads
     * @param ipaddress
     * @param skds
     * @return
     */
    private ArrayList<BlackListThread> createThreadList(int numberThreads, String ipaddress, HostBlacklistsDataSourceFacade skds) {
        int rangeBegin = 0;
        int rangeEnd = skds.getRegisteredServersCount()/numberThreads;
        ArrayList<BlackListThread> threadList = new ArrayList<>();
        for (int i = 0 ; i < numberThreads; i++){
            threadList.add(new BlackListThread(rangeBegin,rangeEnd,ipaddress));
            rangeBegin += skds.getRegisteredServersCount() / numberThreads;
            rangeEnd += skds.getRegisteredServersCount() / numberThreads;
            //Esta condicion es para asegurar recorrer los ultimos valores cuando numberThreads es impar
            if (i==numberThreads-1) rangeEnd = skds.getRegisteredServersCount();
        }
        return threadList;
    }
}
