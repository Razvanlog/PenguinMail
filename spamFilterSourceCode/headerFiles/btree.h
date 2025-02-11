#pragma once
template<typename T>
class BTreeNode{
    private:
    T data;
    int n;
    bool isLeaf;
    public:
    BTreeNode<T>**children;
    T *dkeys;
    BTreeNode(const int initsize){
        dkeys=new T[2*initsize-1];
        children=new BTreeNode<T>*[2*initsize];
        isLeaf=true;
        n=0;
    }
    ~BTreeNode(){
        delete dkeys;
        delete[] children;
    }
    bool Leaf(){
        return isLeaf;
    }
    void setLeaf(bool t){
        isLeaf=t;
    }
    int size(){
        return n;
    }
    void setSize(int t){
        n=t;
    }
    int findKey(T);
};
template<typename T>
class BTree{
    private:
    BTreeNode<T>*root;
    int degree;
    public:
    BTree(int initsize){
        root=allocateNode(initsize);
        degree=initsize;
    }
    void insert(T);
    void IODisplay();
    void Display();
    BTreeNode<T>* getRoot(){
        return root;
    }
    void remove(T);
    void remove(BTreeNode<T>*, T);
    private:
    BTreeNode<T>*allocateNode(int);
    void insertNonfull(BTreeNode<T>*, T);
    void splitChild(BTreeNode<T>*, int, BTreeNode<T>*);
    void inorderDisplay(BTreeNode<T>*);
    void DisplayTreeForm(BTreeNode<T>*, int);
    void fill(BTreeNode<T>*, int);
    void popfromLeaf(BTreeNode<T>*, int);
    void popfromnonLeaf(BTreeNode<T>*, int);
    void merge(BTreeNode<T>*, int);
    void borrowfromLeft(BTreeNode<T>*, int);
    void borrowfromRight(BTreeNode<T>*, int);
    T predecessor(BTreeNode<T>*, T);
    T successor(BTreeNode<T>*, T);
};
#include "btree.ipp"