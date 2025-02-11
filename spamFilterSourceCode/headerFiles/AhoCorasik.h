#pragma once
#include <vector>
#include <map>
#include <unordered_map>
#include <string>
using namespace std;
class TrieNode{
    private:
    TrieNode* failureLink=nullptr;
    int patternID;
    unordered_map<char,TrieNode*> nextMap;
    public:
    int nodeID;
    int depth;
    TrieNode(){patternID=-1;}
    void addEdge(TrieNode*, char);
    void removeEdge(char);
    void setFailureLink(TrieNode*);
    TrieNode* getFL();
    TrieNode* next(char);
    void setPID(int);
    int getPID();
    char* allKeys();
};

class AhoC{
    private:
    TrieNode* root;
    bool built;
    vector<string> patterns;
    void generateFailureKeys();
    void generateKeyTree();
    void displayTree(TrieNode*); 
    public:
    int size;
    AhoC(){
        size=0;
        root=new TrieNode();
        root->setFailureLink(root);
    }
    vector<string> getPatterns(){return patterns;}
    void addPattern(string);
    void build();
    void display();
    vector<pair<int,int>> match(string);
    void debugprint();
};

#include "AhoCorasik.ipp"