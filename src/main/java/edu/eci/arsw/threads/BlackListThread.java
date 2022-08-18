package edu.eci.arsw.threads;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class BlackListThread extends Thread{
    private int rangeBegin,rangeEnd;
    private String ipaddress;
    private ArrayList<Integer> blackListOcurrences;
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    public BlackListThread(int rangeBegin, int rangeEnd, String ipaddress){
        this.rangeBegin = rangeBegin;
        this.rangeEnd = rangeEnd;
        this.ipaddress = ipaddress;
        blackListOcurrences=new ArrayList<>();
    }

    public void run(){
        int checkedListsCount=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        for (int j = rangeBegin; j < rangeEnd; j++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(j, ipaddress)) {
                blackListOcurrences.add(j);
            }
        }
    }

    public ArrayList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }
}
