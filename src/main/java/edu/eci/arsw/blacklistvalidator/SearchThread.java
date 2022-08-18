package edu.eci.arsw.blacklistvalidator;



import java.lang.*;
import java.util.LinkedList;
import java.util.logging.Level;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class SearchThread extends Thread{

    private String ipaddress;
    private int rangeBegin, rangeEnd;
    private int BLACK_LIST_ALARM_COUNT = 5;
    private LinkedList<Integer> blackListOcurrences;

    public SearchThread(String ipaddress, int rangeBegin, int rangeEnd){
        this.ipaddress = ipaddress;
        this.rangeBegin=rangeBegin;
        this.rangeEnd=rangeEnd;
        blackListOcurrences=new LinkedList<>();
    }

    public void run(){
        int ocurrencesCount=0;

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        int checkedListsCount=0;

        for (int i=rangeBegin;i<rangeEnd && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }
        ocurrencesServersMalicious();
    }

    private void ocurrencesServersMalicious(){
        System.out.println("Ocurrencias de servidores maliciosos: "+ blackListOcurrences.size());
    }

}
