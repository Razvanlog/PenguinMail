#pragma once
#include "rbtree.h"
#include <iostream>

template<typename T>
void RBTreeNode<T>::setLeft(RBTreeNode<T> *x){
    left=x;
}

template<typename T>
void RBTreeNode<T>::setRight(RBTreeNode<T> *x){
    right=x;
}

template<typename T>
void RBTreeNode<T>::setParent(RBTreeNode<T> *x){
    parent=x;
}

template<typename T>
void RBTreeNode<T>::setColor(color x){
    col=x;
}

template <typename T>
RBTreeNode<T>* RBTree<T>::find(T data){
    return search(root,data);
}

template <typename T>
void RBTreeNode<T>::setData(T x){
    data=x;
}

template <typename T>
RBTreeNode<T>* RBTree<T>::search(RBTreeNode<T>* x,T data){
    // std::cout<<"YES";
    if (isNil(x) || x->data==data){
        return x;
    }
    return search((x->data>data? x->getLeft(): x->getRight()),data);
}

template <typename T>
void RBTree<T>::leftRotate(RBTreeNode<T> *x){
    RBTreeNode<T> *y=x->getRight();
    x->setRight(y->getLeft());
    if (isNil(y->getLeft())==true){
        y->getLeft()->setParent(x);
    }
    y->setParent(x->getParent());
    if (isNil(x->getParent())==true){
        root=y;
    }
    else if (x==x->getParent()->getLeft()){
        x->getParent()->setLeft(y);
    }
    else{
        x->getParent()->setRight(y);
    }
    y->setLeft(x);
    x->setParent(y);
}

template<typename T>
void RBTree<T>::rightRotate(RBTreeNode<T> *x){
    RBTreeNode<T> *y=x->getLeft();
    x->setLeft(y->getRight());
    if (isNil(y->getRight())==true){
        y->getRight()->setParent(x);
    }
    y->setParent(x->getParent());
    if (isNil(x->getParent())==true){
        root=y;
    }
    else if (x==x->getParent()->getLeft()){
        x->getParent()->setLeft(y);
    }
    else{
        x->getParent()->setRight(y);
    }
    y->setRight(x);
    x->setParent(y);
}

template<typename T>
RBTreeNode<T>* RBTree<T>::minimum(RBTreeNode<T> *root){
    if (!isNil(root->getLeft())){
        return minimum(root->getLeft());
    }
    return root;
}

template<typename T>
RBTreeNode<T>* RBTree<T>::maximum(RBTreeNode<T> *root){
    if (isNil(!root->getRight())){
        return minimum(root->getRight());
    }
    return root;
}

template<typename T>
RBTreeNode<T>* RBTree<T>::find(RBTreeNode<T> *root, T data){
        if (isNil(root)==true || root.data==data){
            return root;
        }
        return find( (data<root.data ? root->getLeft() : root->getRight()), data);
}

template<typename T>
RBTreeNode<T>* RBTree<T>::predecessor(RBTreeNode<T> *root){
    if (isNil(root))
        return root;
    if (!isNil(root->getLeft())){
        return maximum(root->getLeft());
    }
    RBTreeNode<T>*x=root->getParent();
    while (!isNil(x) && root==x->getLeft()){
        root=x;
        x=root->getParent();
    }
    return x;
}

template<typename T>
RBTreeNode<T>* RBTree<T>::successor(RBTreeNode<T> *root){
    if (isNil(root))
        return root;
    if (!isNil(root->getRight())){
        // cout<<"YES";
        return minimum(root->getRight());
    }
    RBTreeNode<T>*x=root->getParent();
    while (!isNil(x) && root==x->getRight()){
        root=x;
        x=root->getParent();
    }
    return x;
}

template<typename T>
void RBTree<T>::insert(T data){
    RBTreeNode<T> *y=RBTreeNode<T>::Nil,
    *x=root;
    // std::cout<<"YEES";
    while (!isNil(x)){
        y=x;
        x=(data < x->getData()) ? x->getLeft() : x->getRight();
    }
    RBTreeNode<T>* z=new RBTreeNode<T>(data);
    z->setParent(y);
    if (isNil(y)){
        root=z;
        // cout<<isNil(root);/
    }
    else if (z->data<y->data){
        y->setLeft(z);
    }
    else y->setRight(z);
    z->setLeft(RBTreeNode<T>::Nil);
    z->setRight(RBTreeNode<T>::Nil);
    z->setColor(RBTreeNode<T>::RED);
    // std::cout<<"YES";
    insertFixup(z);
}

template<typename T>
void RBTree<T>::insertFixup(RBTreeNode<T> *z){
    while (isNil(z->getParent())==false  && isNil(z->getParent()->getParent())==false && z->getParent()->getColor()==RBTreeNode<T>::BLACK){
        // cout<<"yes\n";
        if (z->getParent()==z->getParent()->getParent()->getLeft()){
            // cout<<"yes";
            RBTreeNode<T>* y=z->getParent()->getParent()->getRight();
            if(y->getColor()==RBTreeNode<T>::RED){
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                y->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                z=z->getParent()->getParent();
            }
            else{
                // cout<<"yes";
                if (z==z->getParent()->getRight()){
                    z=z->getParent();
                    leftRotate(z);
                }
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                rightRotate(z->getParent()->getParent());
            }
        }
        else{
            // cout<<"yes";
            RBTreeNode<T>* y=z->getParent()->getParent()->getLeft();
            if (y->getColor()==RBTreeNode<T>::RED){
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                y->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                z=z->getParent()->getParent();
            }
            else{
                // cout<<"yes";
                if (z==z->getParent()->getLeft()){
                    z=z->getParent();
                    rightRotate(z);
                }
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                leftRotate(z->getParent()->getParent());
            }
        }
    }
    root->setColor(RBTreeNode<T>::BLACK);
}

template<typename T>
void RBTree<T>::removeFixup(RBTreeNode<T> *z){
    // cout<<(z==root)<<'\n';
    while (isNil(z->getParent())==false &&  z->getParent()->getColor()==RBTreeNode<T>::BLACK){
        // cout<<"YES\n";
        if (z->getParent()==z->getParent()->getParent()->getLeft()){
            RBTreeNode<T>* y=z->getParent()->getParent()->getRight();
            if (y->getColor()==RBTreeNode<T>::RED){
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                y->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                z=z->getParent()->getParent();
            }
            else{
                if (z==z->getParent()->getRight()){
                    z=z->getParent();
                    leftRotate(z);
                }
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                rightRotate(z->getParent()->getParent());
            }
        }
        else{
            RBTreeNode<T>* y=z->getParent()->getParent()->getLeft();
            if (y->getColor()==RBTreeNode<T>::RED){
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                y->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                z=z->getParent()->getParent();
            }
            else{
                if (z==z->getParent()->getLeft()){
                    z=z->getParent();
                    rightRotate(z);
                }
                z->getParent()->setColor(RBTreeNode<T>::BLACK);
                z->getParent()->getParent()->setColor(RBTreeNode<T>::RED);
                leftRotate(z->getParent()->getParent());
            }
        }
    }
    // cout<<"DA\n";
    root->setColor(RBTreeNode<T>::BLACK);
}

template<typename T>
int RBTree<T>::remove(T data){
    RBTreeNode<T> *it=find(data);
    if (isNil(it))
        return 1;
    // cout<<"yes\n";
    // cout<<it->getData()<<' '<<it->getLeft()->getData()<<' '<<it->getRight()->getData()<<'\n';
    RBTreeNode<T> *y=(isNil(it->getLeft()) || isNil(it->getRight()))?it:successor(it),
    *x=(!isNil(y->getLeft()))? y->getLeft(): y->getRight();
    x->setParent(y->getParent());
    // cout<<y->getData()<<'\n';
    if (isNil(y->getParent())){
        // cout<<"yes\n";
        root=x;
    }
    else{
        if (y==y->getParent()->getLeft())
            y->getParent()->setLeft(x);
        else y->getParent()->setRight(x);
    }
    if (y!=it)
        it->data=y->data;
    // display(root);
    if (y->col==RBTreeNode<T>::BLACK){
         removeFixup(x);   
    }
    return 0;
}

template<typename T>
vector<T> RBTree<T>::inorderList(){
    vector<T>*v=new vector<T>;
    inordertrav(root,v);
    return *v;
}

template<typename T>
void RBTree<T>::display(RBTreeNode<T>*p){
    if (isNil(p)==true){
        return;
    }
    if (isNil(p->getLeft())==false){
        display(p->getLeft());
    }
    cout<<p->getData()<<'\n';
    // v->push_back(p->getData());
    if (isNil(p->getRight())==false){
        display(p->getRight());
    }
}

template<typename T>
void RBTree<T>::inordertrav(RBTreeNode<T>*p,vector<T>*v){
    if (isNil(p)==true){
        return;
    }
    if (isNil(p->getLeft())==false){
        inordertrav(p->getLeft(),v);
    }
    v->push_back(p->getData());
    if (isNil(p->getRight())==false){
        inordertrav(p->getRight(),v);
    }
}