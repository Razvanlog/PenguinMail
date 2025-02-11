#include <iostream>
#include "headerFiles/btree.h"
#include "headerFiles/rbtree.h"
#include "headerFiles/tokenClass.h"
#include "headerFiles/AhoCorasik.h"
#include <fstream>
#include <filesystem>
#include <limits>
#define FILE_NAME ("badWords.txt")
#define BAD_PROB_DEF (0.8)
#define GOOD_PROB_DEF (0.01)
#define SPAM_PROB_DEF (0.05)
ifstream f;
fstream storage;
using namespace std;
int commandMatch(string);
void toLower(string*);
void checkText(RBTree<tokenSpam>*);
void addwordsm(RBTree<tokenSpam>*);
void removewordsm(RBTree<tokenSpam>*);
void endp(RBTree<tokenSpam>*);
void checkFile(RBTree<tokenSpam>*);
void checkfileApp(RBTree<tokenSpam>*,fstream*);
long double BAD_PROB,GOOD_PROB,SPAM_PROB;
int main(int argc, char **argv){
    fstream conf;
    long double a,b,c;
    conf.open("configsf",std::fstream::in);
    if (conf){
    conf>>a>>b>>c;
    }
    else{
        a=BAD_PROB_DEF;
        b=GOOD_PROB_DEF;
        c=SPAM_PROB_DEF;
    }
    BAD_PROB=a,GOOD_PROB=b,SPAM_PROB=c;
    // if (argc==2){
    //     f.open(argv[1]);
    //     if (!f.is_open()){
    //         cerr<<"file is not valid\n";
    //         return 1;
    //     }
    // }
    // else {
    //     cerr<<"error number of arguments\n";
    //     return 1;
    // }
    // RBTree<int> test;
    // test.insert(1);
    // test.insert(2);
    // test.insert(0);
    // test.insert(4);
    // test.insert(5);
    // test.insert(3);
    // // test.display(test.getRoot());
    // // cout<<'\n';
    // // cout<<test.isNil(test.getRoot()->getRight());
    // // cout<<test.successor(test.getRoot())->getData();
    // // test.remove(1);
    // // cout<<test.isNil(test.getRoot());
    // // test.display(test.getRoot());
    // // cout<<"YES";

    // AhoC t;
    // t.addPattern("yes");
    // t.addPattern("no");
    // t.debugprint();
    // cout<<"YES\n";
    // vector<pair<int,int>> vanswer=t.match("yesn omcas");
    // for (auto i:vanswer){
    //     cout<<i.first<<' '<<i.second<<'\n';
    // }
    storage.open(FILE_NAME,std::fstream::in);
    if (!storage){
        cerr<<"Error creating storage\n";
        return 1;
    }
    RBTree<tokenSpam> rb;
    string line;
    while (getline(storage,line)){
        // cout<<line<<'\n';
        int pos=line.find(',');
        double prob=atof(line.substr(pos+1,line.size()-1).c_str());
        string token=line.substr(0,pos);
        // cout<<token<<' '<<prob<<'\n';
        toLower(&line);
        rb.insert({token,prob});
    }
    if (argc==2){
        fstream fi;
        fi.open(argv[1],std::fstream::in);
        if (!fi){
            exit(1);
        }
        checkfileApp(&rb,&fi);
        exit(0);
    }
    // rb.display(rb.getRoot());
    // cout<<'\n';
    while (1){
        string command;
        cout<<"enter command>";
        cin>>command;
        int r=commandMatch(command);
        switch (r){
            case 0:
            endp(&rb);
            break;
            case 1:
            addwordsm(&rb);
            break;
            case 2:
            removewordsm(&rb);
            break;
            case 3:
            checkText(&rb);
            break;
            case 4:
            checkFile(&rb);
            break;
            case 5:
            rb.display(rb.getRoot());
            cout<<'\n';
            break;
            default:
            endp(&rb);
        }
    }
    // }
    // string text,x;
    // while (f>>x){
    //     text=text+" "+x;
    // }
    endp(&rb);
    return 0;
}
int commandMatch(string s){
    if (s=="addsm"){
        return 1;
    }
    if (s=="removesm")
        return 2;
    if (s=="checktext")
        return 3;
    if (s=="checkfile")
        return 4;
    if (s=="displaysw")
        return 5;
    if (s=="exit")
        return 0;
    return -1;
}

void addwordsm(RBTree<tokenSpam>*rb){
    string t;
    cout<<"write word to be added to spam filter: ";
    cin>>t;
    cout<<"write probability of being spam: ";
    double d;
    cin>>d;
    toLower(&t);
    RBTreeNode<tokenSpam>*p=rb->find({t,d});
    if (rb->isNil(p)==false)
        return;
    rb->insert({t,d});
}

void removewordsm(RBTree<tokenSpam>*rb){
    string t;
    cout<<"write word to be deleted from spam filter: ";
    cin>>t;
    toLower(&t);
    RBTreeNode<tokenSpam>*p=rb->find({t,0});
    if (rb->isNil(p)==true){
        cout<<"word is not present in spam filter.\n";
        return;
    }
    rb->remove({t,0});
}

void checkText(RBTree<tokenSpam>*rb){
    AhoC c;
    vector<tokenSpam> v=rb->inorderList(); 
    for (auto i:v){
        c.addPattern(i.getWord());
    }
    c.build();
    // c.debugprint();
    string st;
    cout<<"write text:";
    int ok=0;
    cin.ignore(numeric_limits<streamsize>::max(),'\n');
    char* t=new char[1001];
    cin.getline(t,sizeof(char)*1001);
    int nr=0;
    char * p=strtok(t,", ");
    string word;
    long double prob=1,a1=1;
    while (p!=NULL){
        // nr++;
        word=p;
        toLower(&word);
        vector<pair<int,int>> answer=c.match(word);
        p=strtok(NULL,", ");
        if (answer.size()==0){
            prob=prob*(GOOD_PROB);
            a1=a1*(1-GOOD_PROB);
            continue;
        }
        for (int i=0; i<answer.size(); i++){
            RBTreeNode<tokenSpam>*p=rb->find({c.getPatterns()[answer[i].first],0});
            cout<<p->getData().getWord()<<' '<<p->getData().getProb()<<' '<<c.getPatterns()[answer[i].first]<<'\n';
            prob=prob*(p->getData().getProb());
            a1=a1*(1-p->getData().getProb());
        }
    }
    prob=prob/(prob+a1);
    cout<<prob<<'\n';
    if (prob>=SPAM_PROB){
        cout<<1<<'\n';
        return;
    }
    cout<<0<<'\n';
    // cout<<st+"\n";
    // vector<pair<int,int>> answer=c.match(st);
    // long double spam_prob=1;
    // if (answer.size()>=nr/3){
    //     cout<<"message is likely spam\n";
    // }
    // else cout<<"message is likely not spam\n";
    // cout<<st+'\n';
    // for (auto i:answer){
    //     cout<<c.getPatterns()[i.first]<<" at position"<<' '<<i.second<<'\n';
    // }
}

void checkFile(RBTree<tokenSpam>*rb){
    AhoC c;
    vector<tokenSpam> v=rb->inorderList();
    for (auto i:v){
        c.addPattern(i.getWord());
    }
    cout<<"write name of file: ";
    string filename;
    cin>>filename;
    filesystem::path file=filename;
    c.build();
    string text,word;
    cout<<"reading from "<<file<<'\n';
    fstream fread(file,std::fstream::in);
    if (!fread){
        cout<<"could not find file...\n";
        return;
    }
    int nr=0;
    long double prob=1,a1=1;
    while (fread>>word){
        toLower(&word);
        vector<pair<int,int>> answer=c.match(word);
        if (answer.size()==0){
            prob=prob*(GOOD_PROB);
            a1=a1*(1-GOOD_PROB);
            continue;
        }
        for (int i=0; i<answer.size(); i++){
            RBTreeNode<tokenSpam>*p=rb->find({c.getPatterns()[answer[i].first],0});
            prob=prob*(p->getData().getProb());
            a1=a1*(1-p->getData().getProb());
        }
    }
    prob=prob/(prob+a1);
    cout<<prob<<'\n';
    if (prob>=SPAM_PROB){
        cout<<1<<'\n';
    }
    else cout<<0<<'\n';
    // vector<pair<int,int>> answer=c.match(text);

    // long double prob=answer.size()/nr;
    // if (prob){
    //     cout<<"message is likely spam\n";
    // }
    // else {
    //     cout<<"message is likely not spam\n";
    // }
}

void checkfileApp(RBTree<tokenSpam>*rb,fstream *f){
    AhoC c;
    vector<tokenSpam> v=rb->inorderList();
    for (auto i:v){
        c.addPattern(i.getWord());
    }
    c.build();
    string text,word;
    int nr=0;
    long double prob=1,a1=1;
    while (*f>>word){
        toLower(&word);
        vector<pair<int,int>> answer=c.match(word);
        if (answer.size()==0){
            prob=prob*(GOOD_PROB);
            a1=a1*(1-GOOD_PROB);
            continue;
        }
        for (int i=0; i<answer.size(); i++){
            RBTreeNode<tokenSpam>*p=rb->find({c.getPatterns()[answer[i].first],0});
            prob=prob*(p->getData().getProb());
            a1=a1*(1-p->getData().getProb());
        }
    }
    f->close();
    // vector<pair<int,int>> answer=c.match(text);
    fstream outfile;
    outfile.open("a.out",std::fstream::out);
    if (!outfile){
        exit(1);
    }
    prob=prob/(prob+a1);
    if (prob>=SPAM_PROB){
        outfile<<"1";
    }
    else {
        outfile<<"0";
        // cout<<"message is likely not spam\n";
    }
    outfile.close();
}

void endp(RBTree<tokenSpam>*rb){
    storage.close();
    storage.open(FILE_NAME,std::fstream::out);
    vector<tokenSpam> v=rb->inorderList();
    // ofstream g;
    // fopen("badWords.txt");
    storage.seekp(0);
    // cout<<v.size()<<'\n';
    for (int i=0; i<v.size(); i++){
        storage<<v[i].getWord()<<","<<v[i].getProb()<<'\n';
        // cout<<v[i].getWord()<<'\n';
    }
    storage.close();
    exit(0);
}

void toLower(string *s){
    for (int i=0; i<s->size(); i++){
        if (s->at(i)>='A' && s->at(i)<='Z'){
            (*s)[i]=s->at(i)-'A'+'a';
        }
    }
}