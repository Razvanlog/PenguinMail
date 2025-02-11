#pragma once
#include <string>
using namespace std;
class tokenSpam{
    string a;
    int spamprob;
    double prob;
    public:
    tokenSpam(string x=""){
        a=x;
        prob=0;
        spamprob=x.length();
    }
    tokenSpam(string x,double xprob=0){
        prob=xprob;
        a=x;
        spamprob=x.length();
    }
    string getWord() const{
        return a;
    }
    double getProb()const{
        return prob;
    }
    bool operator<(tokenSpam x){
        if (this->spamprob<x.spamprob){
            return 1;
        }
        return 0;
    }
    bool operator>(tokenSpam x){
        if (this->spamprob>x.spamprob){
            return 1;
        }
        return 0;
    }
    bool operator==(tokenSpam x){
        if (this->a!=x.a){
            return 0;
        }
        return 1;
    }
    friend ostream& operator<<(ostream& os, const tokenSpam& dt);
};

ostream& operator<<(ostream& os, const tokenSpam& dt)
{
    os << dt.a;
    return os;
}