#pragma once
#include "AhoCorasik.h"
#include <cstring>
#include <iostream>
#include <queue>
void TrieNode::addEdge(TrieNode *p,char c){
    nextMap[c]=p;
}

void TrieNode::removeEdge(char c){
    nextMap.erase(c);
}

void TrieNode::setFailureLink(TrieNode* p){
    failureLink=p;
}

TrieNode* TrieNode::getFL(){
    return failureLink;
}

TrieNode* TrieNode::next(char c){
    if (nextMap.count(c)==1)
        return nextMap[c];
    return nullptr;
}

char* TrieNode::allKeys(){
    if (nextMap.size()>0){
    char* answer=new char[nextMap.size()];
    memset(answer,0,sizeof(answer));
    int it=0;
    for (auto i=nextMap.begin(); i!=nextMap.end(); i++){
        answer[it]=i->first;
        it++;
    }
    return answer;
    }
    return nullptr;
}

void TrieNode::setPID(int x){
    patternID=x;
}

int TrieNode::getPID(){
    return patternID;
}

void AhoC::addPattern(string x){
    patterns.push_back(x);
}

void AhoC::generateFailureKeys(){
    struct point{
        TrieNode* p,* parent;
        char x;
        int depth;
    } start;
    // start.p=root;
    queue<point> q;
    start.parent=root;
    start.depth=1;
    char *rkeys=root->allKeys();
    if (rkeys==nullptr){
        return;
    }
    for (int i=0; i<strlen(rkeys); i++){
        TrieNode* next=root->next(rkeys[i]);
        start.p=next;
        start.x=rkeys[i];
        q.push(start);
    }
    // for (int i=0; i<strlen)
    // start.depth=0;
    
    q.push(start);
    while (!q.empty()){
        point first=q.front();
        // std::cout<<first.depth<<'\n';
        q.pop();
        if (first.depth==1){
            first.p->setFailureLink(root);
            char*keys=first.p->allKeys();
            if (keys==nullptr)
            continue;
            for (int i=0; i<strlen(keys); i++){
                TrieNode* next=first.p->next(keys[i]);
                point toAdd;
                toAdd.depth=first.depth+1;
                toAdd.p=next;
                toAdd.parent=first.p;
                toAdd.x=keys[i];
                q.push(toAdd);
            }
            delete(keys);
            continue;
        }
        if (first.depth>1){
        TrieNode* fallBack=first.parent->getFL();
        while (fallBack!=root && fallBack->next(first.x)==nullptr){
            fallBack=fallBack->getFL();
        }
        if (fallBack->next(first.x)!=nullptr){
            first.p->setFailureLink(fallBack->next(first.x));
        }
        else first.p->setFailureLink(root);
        char*keys=first.p->allKeys();
        // cout<<"goingthroughfailurekeys\n";
        if (keys==nullptr){
            continue;
        }
        for (int i=0; i<strlen(keys); i++){
            // TrieNode *fallBack=first.parent->getFL();
            
            // while (fallBack!=root && fallBack->next(keys[i])==nullptr){
            //     fallBack=fallBack->getFL();
            // }
            // if (fallBack->next(keys[i])!=nullptr){
            //     first.p->setFailureLink(fallBack->next(keys[i]));
            // }
            // else{
            //     first.p->setFailureLink(root);
            // }
            point toAdd;
            toAdd.depth=first.depth+1;
            toAdd.p=first.p->next(keys[i]);
            toAdd.parent=first.p;
            toAdd.x=keys[i];
            q.push(toAdd);
            // point toAdd;
            // toAdd.depth=first.depth+1;
            // toAdd.p=first.p->next(keys[i]);
            // toAdd.parent=first.p;
            // q.push(toAdd);
        }
        delete keys;
        }
    }
}

void AhoC::generateKeyTree(){
    for (int i=0; i<patterns.size(); i++){
        auto it=root;
        for (int j=0; j<patterns[i].size(); j++){
            TrieNode* nextLocation=it->next(patterns[i][j]);
            if (nextLocation!=nullptr){
                it=nextLocation;
            }
            else{
                it->addEdge(new TrieNode(),patterns[i][j]);
                nextLocation=it->next(patterns[i][j]);
                it=nextLocation;
                // it->setFailureLink(root);
                it->nodeID=size++;
            }
        }
        it->setPID(i);
    }
}

void AhoC::build(){
    generateKeyTree();
    // std::cout<<"DONE\n";
    generateFailureKeys();
    built=1;
}

void AhoC::displayTree(TrieNode* it){
    if (it->getPID()>=0){
        // std::cout<<'\n';
        return;
    }
    char* keys=it->allKeys();
    if (keys==nullptr){
        std::cout<<'\n';
        return;
    }
    int failurelinknr=0;
    if (it->getFL()==nullptr){
        failurelinknr=-1;
    }
    else{
        failurelinknr=it->getFL()->nodeID;
    }
    cout<<it->nodeID<<" failure node:"<<failurelinknr<<'\n';
    for (int i=0; i<strlen(keys); i++){
        // std::cout<<keys[i];
        auto next=it->next(keys[i]);
        displayTree(next);
    }
    delete(keys);
}

vector<pair<int,int>> AhoC::match(string Text){
    vector<pair<int,int>>answer;
    int c=0,l=0;
    TrieNode* w=root;
    do{
        int savec=c;
        while (w->next(Text[c])){
            if (w->next(Text[c])->getPID()>=0){
                answer.push_back({w->next(Text[c])->getPID(),c-patterns[w->next(Text[c])->getPID()].size()+1});
            }
            w=w->next(Text[c]);
            c++;
        }
        w=w->getFL();
        if (savec==c)
        c++;
    }while(c<Text.size());
    return answer;
}

void AhoC::display(){
    displayTree(root);
}

void AhoC::debugprint(){
    queue<pair<TrieNode*,string>> q;
    q.push({root,""});
    while (!q.empty()){
        auto [node,prefix]=q.front();
        q.pop();
        cout<<node->nodeID<<' '<<node->getPID()<<' '<<prefix<<' ';
        if (node->getFL()!=nullptr)
        cout<<node->getFL()->nodeID<<'\n';
        else cout<<-1<<'\n';
        char*keys=node->allKeys();
        if (keys==nullptr){
            continue;
        }
        for (int i=0; i<strlen(keys); i++){
            string p=prefix+keys[i];
            q.push({node->next(keys[i]),p});
        }
        delete keys;
    }
}