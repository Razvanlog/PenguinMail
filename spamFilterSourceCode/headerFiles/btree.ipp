#pragma once
#include "btree.h"
#include <iostream>
//Node Methods

template<typename T>
int BTreeNode<T>::findKey(T k){
        int i=0;
        while (i<n && dkeys[i]<k){
            i++;
        }
        return i;
        // int li=0,ls=n-1,lm=0,lastgoodanswer=-1;
        // while (li<ls){
        //     lm=(li+ls)/2;
        //     if (dkeys[lm]<=k){
        //         lastgoodanswer=lm;
        //         li=lm+1;
        //     }
        //     else{
        //         ls=lm-1;
        //     }
        // }
        // return lastgoodanswer;
}

//Btree Methods
template<typename T>
BTreeNode<T>* BTree<T>::allocateNode(int initsize){
    return new BTreeNode<T>(initsize);
}

template<typename T>
void BTree<T>::insertNonfull(BTreeNode<T>*p, T data){
    int i=p->size()-1;
    if (p->Leaf()){
        while (i>=0 && data<(p->dkeys)[i]){
            (p->dkeys)[i+1]=(p->dkeys)[i];
            i--;
        }
        (p->dkeys)[i+1]=data;
        p->setSize(p->size()+1);
        //diskwrite
    }
    else{
        while (i>=0 && data<(p->dkeys)[i]){
            i--;
        }
        i++;
        //diskread
        if ((p->children)[i]->size()==2*degree-1){
            splitChild(p,i,(p->children)[i]);
            if (data>(p->dkeys)[i]){
                i++;
            }
        }
        insertNonfull((p->children[i]),data);
    }
}

template<typename T>
void BTree<T>::insert(T data){
    BTreeNode<T> *it=root;
    if (it->size()==2*degree-1){
        BTreeNode<T>*s=allocateNode(degree);
        root=s;
        s->setLeaf(false);
        s->setSize(0);
        (s->children)[0]=it;
        splitChild(s,0,it);
        insertNonfull(s,data);
    }
    else{
        insertNonfull(it,data);
    }
}

template<typename T>
void BTree<T>::splitChild(BTreeNode<T>*x, int poz, BTreeNode<T>*y){
    BTreeNode<T>*z=allocateNode(degree);
    z->setLeaf(y->Leaf());
    z->setSize(degree-1);
    for (int j=0; j<degree-1; j++){
        (z->dkeys)[j]=(y->dkeys)[j+degree];
    }
    if (!(y->Leaf())){
        for (int j=0; j<degree-1; j++){
            (z->children)[j]=(y->children)[j+degree];
        }
    }
    y->setSize(degree-1);
    for (int j=x->size(); j>=poz; j--){
        (x->children)[j+1]=(x->children)[j];
        (x->dkeys)[j]=(x->dkeys)[j-1];
    }
    (x->children)[poz+1]=z;
    (x->dkeys)[poz]=(y->dkeys)[degree-1];
    x->setSize(1+x->size());
    //disk write for x,y,z.
}

template<typename T>
void BTree<T>::IODisplay(){
    inorderDisplay(root);
}

template<typename T>
void BTree<T>::inorderDisplay(BTreeNode<T>* p){
    bool b=p->Leaf();
    for (int i=0; i<p->size(); i++){
        if (!b){
            inorderDisplay((p->children)[i]);
        }
        std::cout<<(p->dkeys)[i]<<' ';
    }
    if (!b){
        inorderDisplay((p->children)[p->size()]);
    }
}
template<typename T>
void BTree<T>::Display(){
    DisplayTreeForm(root,0);
}
template<typename T>
void BTree<T>::DisplayTreeForm(BTreeNode<T>*p,int indent){
    if (p==nullptr){
        return;
    }
    bool b=p->Leaf();
    for (int i=0; i<p->size(); i++){
        if (!b){
            DisplayTreeForm((p->children)[i],indent+2);
        }
        for (int j=0; j<indent; j++){
            std::cout<<" ";
        }
        std::cout<<(p->dkeys)[i]<<'\n';
    }
    if (!b){
        DisplayTreeForm((p->children)[p->size()],indent+2);
    }
}

template<typename T>
void BTree<T>::remove(T data){
    if (root==nullptr){
        return;
    }
    remove(root, data);
    if (root->size()==0){
        BTreeNode<T>*tmp=root;
        if (tmp->Leaf()){
            root=nullptr;
        }
        else root=(root->children)[0];
        delete tmp;
    }
}

template<typename T>
void BTree<T>::remove(BTreeNode<T>* p, T data){
    int i=p->findKey(data);
    if (i<p->size() && p->dkeys[i]==data){
        if (p->Leaf()){
            popfromLeaf(p,i);
        }
        else{
            popfromnonLeaf(p,i);
        }
        return;
    }
    if (p->Leaf()){
        return;
    }
    bool last=((i==p->size())?true:false);
    if (p->children[i]->size()<degree){
        fill(p,i);
    }
    if (last && i>p->size()){
        remove(p->children[i-1],data);
    }
    else{
        remove(p->children[i],data);
    }
}

template<typename T>
void BTree<T>::popfromLeaf(BTreeNode<T>* p, int poz){
    for (int i=poz+1; i<p->size(); i++){
        p->dkeys[i-1]=p->dkeys[i];
    }
    p->setSize(p->size()-1);
}

template<typename T>
void BTree<T>::popfromnonLeaf(BTreeNode<T>*p, int poz){
    T k=p->dkeys[poz];
    if (p->children[poz]->size()>=degree){
        T pred=predecessor(p->children[poz],k);
        p->dkeys[poz]=pred;
        remove(p->children[poz],pred);
        return;
    }
    if (p->children[poz+1]->size()>=degree){
        T succ=successor(p->children[poz+1],k);
        p->dkeys[poz]=succ;
        remove(p->children[poz+1],succ);
        return;
    }
    merge(p, poz);
    remove(p->children[poz], k);
}

template<typename T>
void BTree<T>::merge(BTreeNode<T>* p,int poz){
    BTreeNode<T>*leftChild=p->children[poz],*rightChild=p->children[poz+1];
    leftChild->dkeys[degree-1]=p->dkeys[poz];
    for (int i=0; i<rightChild->size(); i++){
        leftChild->dkeys[i+degree]=rightChild->dkeys[i];
    }
    if (!leftChild->Leaf()){
        for (int i=0; i<=rightChild->size(); i++){
            leftChild->children[i+degree]=rightChild->children[i];
        }
    }
    for (int i=poz+1; i<p->size(); i++){
        p->dkeys[i-1]=p->dkeys[i];
    }
    for (int i=poz+2; i<=p->size(); i++){
        p->children[i-1]=p->children[i];
    }
    leftChild->setSize(leftChild->size()+rightChild->size()+1);
    p->setSize(p->size()-1);
    delete rightChild;
}

template<typename T>
void BTree<T>::borrowfromLeft(BTreeNode<T>* p, int poz){
    BTreeNode<T>*child=p->children[poz],
    *sibling=p->children[poz-1];
    for (int i=child->size()-1; i>=0; i--){
        child->dkeys[i+1]=child->dkeys[i];
    }
    if (!child->Leaf()){
        for (int i=child->size(); i>=0; i--){
            child->children[i+1]=child->children[i];
        }
    }
    child->dkeys[0]=p->dkeys[poz-1];
    if (!child->Leaf()){
        child->children[0]=sibling->children[sibling->size()];
    }
    p->dkeys[poz-1]=sibling->dkeys[sibling->size()-1];
    child->setSize(child->size()+1);
    sibling->setSize(sibling->size()-1);
}

template<typename T>
void BTree<T>::borrowfromRight(BTreeNode<T>*p, int poz){
    BTreeNode<T>*child=p->children[poz],
    *sibling=p->children[poz+1];
    for (int i=child->size()-1; i>=0; i--){
        child->dkeys[i+1]=child->dkeys[i];
    }
    if (!child->Leaf()){
        for (int i=child->size(); i>=0; i--){
            child->children[i+1]=child->children[i];
        }
    }
    child->dkeys[0]=p->dkeys[poz-1];
    if (!child->Leaf()){
        child->children[0]=sibling->children[sibling->size()];
    }
    p->dkeys[poz-1]=sibling->dkeys[sibling->size()-1];
    child->setSize(child->size()+1);
    sibling->setSize(sibling->size()-1);
}

template<typename T>
void BTree<T>::fill(BTreeNode<T>* p, int poz){
    if (poz!=0 && p->children[poz-1]->size()>=degree){
        borrowfromLeft(p, poz);
        return;
    }
    if (poz!=p->size() && p->children[poz+1]->size()>=degree){
        borrowfromRight(p, poz);
        return;
    }
    if (poz!=p->size()){
        merge(p, poz);
    }
    else merge(p, poz-1);
}

template<typename T>
T BTree<T>::predecessor(BTreeNode<T>* p, T data){
    T*voidptr=nullptr;
    if (p!=nullptr && p->size()>0){
        T maxmin=p->dkeys[0],temp;
        for (int i=0; i<p->size(); i++){
            if (p->dkeys[i]>=data){
                break;
            }
            if (maxmin<p->dkeys[i]){
                maxmin=p->dkeys[i];
                if (p->children[i]!=nullptr && p->children[i]->size()>0)
                temp=predecessor(p->children[i],data);
                maxmin=(temp>maxmin?temp:maxmin);
            }
        }
        return maxmin;
    }
    return *voidptr;
}

template<typename T>
T BTree<T>::successor(BTreeNode<T>* p, T data){
    T*voidptr=nullptr;
    if (p!=nullptr && p->size()>0){
        T minmax=p->dkeys[p->size()-1],temp;
        for (int i=p->size()-1; i>=0; i--){
            if (p->dkeys[i]<=data){
                break;
            }
            if (minmax>p->dkeys[i]){
                minmax=p->dkeys[i];
                if (p->children[i]!=nullptr && p->children[i]->size()>0)
                temp=successor(p->children[i],data);
                minmax=(temp<minmax?temp:minmax);
            }
        }
        return minmax;
    }
    return *voidptr;
}