#pragma once
#include <iostream>
#include <vector>
using namespace std;
template<typename T>
class RBTree;

template<typename T>
class RBTreeNode{
    public:
    enum color{RED, BLACK};
    private:
    T data;
    RBTreeNode<T> *left, *right, *parent;
    bool sentNil;
    color col;
    void setLeft(RBTreeNode<T>*);
    void setRight(RBTreeNode<T>*);
    void setParent(RBTreeNode<T>*);
    void setColor(color);
    void setData(T);
    public:
    T getData(){
        return data;
    }
    static RBTreeNode<T> *Nil;
    RBTreeNode(T c,RBTreeNode<T> *l=Nil, RBTreeNode<T> *r=Nil, RBTreeNode<T> *p=Nil,
     color nc=RBTreeNode::BLACK,bool nilcond=false): data(c),left(l),right(r),parent(p),col(nc),sentNil(nilcond){
        setLeft(Nil);
        setRight(Nil);
        setParent(Nil);
     }
    ~RBTreeNode(){
        if (this!=Nil){
            if (left!=Nil)
            delete left;
            if (right!=Nil)
            delete right;
        }
        // delete data;
    }
    RBTreeNode<T>* getLeft(){
        return left;
    }
    RBTreeNode<T>* getRight(){
        return right;
    }
    RBTreeNode<T>* getParent(){
        return parent;
    }
    color getColor(){
        return col;
    }
    friend RBTree<T>;
};

template<typename T>
RBTreeNode<T>* RBTreeNode<T>::Nil=new RBTreeNode<T> (*(new T()));
// template<typename T>
// RBTreeNode<T>::Nil=new RBTreeNode<T>(new T,Nil,Nil,Nil,RBTreeNode::BLACK);
template<typename T>
class RBTree{
    private:
    RBTreeNode<T>* root;
    void leftRotate(RBTreeNode<T>* );
    void rightRotate(RBTreeNode<T>* );
    void insertFixup(RBTreeNode<T>* );
    void removeFixup(RBTreeNode<T>* );
    void inordertrav(RBTreeNode<T>*,vector<T>*);
    public:
    RBTree(){
        // std::cout<<"YES";
        RBTreeNode<T>::Nil->setLeft(RBTreeNode<T>::Nil);
        RBTreeNode<T>::Nil->setRight(RBTreeNode<T>::Nil);
        RBTreeNode<T>::Nil->setParent(RBTreeNode<T>::Nil);
        root=RBTreeNode<T>::Nil;
    }
    ~RBTree(){
        delete root;
    }
    void display(RBTreeNode<T>*);
    RBTreeNode<T>* find(T);
    RBTreeNode<T>* search(RBTreeNode<T>*, T);
    RBTreeNode<T>* minimum(RBTreeNode<T> *);
    RBTreeNode<T>* maximum(RBTreeNode<T> *);
    RBTreeNode<T>* find(RBTreeNode<T> *, T );
    RBTreeNode<T>* predecessor(RBTreeNode<T> *);
    RBTreeNode<T>* successor(RBTreeNode<T> *);
    vector<T> inorderList();
    void insert(T);
    int remove(T);
    RBTreeNode<T>*getRoot(){
        return root;
    }
    bool isNil(RBTreeNode<T> *p){
        return (RBTreeNode<T>::Nil==p);
    }
};

#include "rbtree.ipp"