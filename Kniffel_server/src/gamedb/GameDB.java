/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamedb;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.HashMap;

/**
 * Class for handle all game data
 *
 * @author carst
 */
public class GameDB {

    /**
     * variable for access locking
     */
    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * list of connected user
     */
    public final ArrayList<DataConnectedUser> connectedUserList;
    public HashMap<String, Integer> dice_sheet;

    /**
     * maximal number od players
     */
    private final int maxPlayers;

    /**
     * maximal number of users
     */
    private final int maxUsers;

    /**
     * Constructor of class
     *
     * @param MAX_USERS
     * @param MAX_PLAYERS
     */
    public GameDB(int MAX_USERS, int MAX_PLAYERS) {
        this.maxUsers = MAX_USERS;
        this.maxPlayers = MAX_PLAYERS;
        connectedUserList = new ArrayList<>();
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    /**
     * get number of connected users
     *
     * @return number of connected users
     */
    public int getNumberOfConnectedUsers() {
        try {
            readLock.lock();
            int size = connectedUserList.size();
            return size;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * get data of a connected user depending on the socket
     *
     * @param socket
     * @return data of connected user
     * @throws GameDBUnknownUserException throws if no connected user with
     * that socket
     */
    private DataConnectedUser getConnectedUser(Socket socket) throws GameDBUnknownUserException {
        for (DataConnectedUser connectedUser : connectedUserList) {
            if (connectedUser.getSocket() == socket) {
                return connectedUser;
            }
        }
        throw new GameDBUnknownUserException();
    }

    /**
     * delete connected user
     * @param socket
     * @throws GameDBUnknownUserException
     */
    public void deleteConnectedUser(Socket socket) throws GameDBUnknownUserException {
        try {
            writeLock.lock();
            connectedUserList.remove(this.getConnectedUser(socket));
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * add connected user
     * @param clientSocket
     * @throws GameDBUserExistsException
     * @throws GameDBToMuchPlayersException
     */
    public void addConnectedUser(Socket clientSocket) throws GameDBUserExistsException, GameDBToMuchPlayersException {
        try {
            writeLock.lock();
            if (connectedUserList.size() >= maxUsers) {
                throw new GameDBToMuchPlayersException();
            }
            try {
                getConnectedUser(clientSocket);
                throw new GameDBUserExistsException();
            } catch (GameDBUnknownUserException ex) {
                DataConnectedUser user = new DataConnectedUser(clientSocket);
                connectedUserList.add(user);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * rename connected user
     * @param clientSocket
     * @param nickname
     * @throws GameDBUnknownUserException
     * @throws GameDBUnsupportedCharacters
     */
    public void renameConnectedUser(Socket clientSocket, String nickname) throws GameDBUnknownUserException, GameDBUnsupportedCharacters {
        try {
            writeLock.lock();
            Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");

            if (p.matcher(nickname).matches()) {
                try {
                    getConnectedUser(clientSocket).setNickname(nickname);
                } catch (GameDBUnknownUserException ex) {
                    throw new GameDBUnknownUserException();
                }
            } else {
                throw new GameDBUnsupportedCharacters();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * get connected user nickname by index
     * @param index
     * @return
     * @throws GameDBUnknownUserException
     */
    public String getConnectedUserNichname(int index) throws GameDBUnknownUserException {
        try {
            readLock.lock();
            if ((index < 0) || (index > connectedUserList.size())) {
                throw new GameDBUnknownUserException();
            }
            return this.connectedUserList.get(index).getNickname();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * get connected user nickname by socket
     * @param clientSocket
     * @return
     * @throws GameDBUnknownUserException
     */
    public String getConnectedUserNichname(Socket clientSocket) throws GameDBUnknownUserException {
        try {
            readLock.lock();
            return this.getConnectedUser(clientSocket).getNickname();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * get connected user socket by index
     * @param index
     * @return
     * @throws GameDBUnknownUserException
     */
    public Socket getConnectedUserSocket(int index) throws GameDBUnknownUserException {
        try {
            readLock.lock();
            if ((index < 0) || (index > connectedUserList.size())) {
                throw new GameDBUnknownUserException();
            }
            return this.connectedUserList.get(index).getSocket();
        } finally {
            readLock.unlock();
        }
    }

    public void send(String s)
    {

        for(DataConnectedUser n : connectedUserList)
        {
            n.send
        }


    }
 
}
