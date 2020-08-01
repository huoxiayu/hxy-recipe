## git官网
https://git-scm.com/book/zh/v2

## git help
git help --web log

## git config相关
#### git config的key必须带. 如user.name
#### git config的作用域
    git config --local  针对当前仓库生效
    git config --global 针对当前用户（所有仓库）生效，更常用
    git config --system 针对系统所有登录用户生效
#### 查询配置
    git config --list --local
    git config --list --global
    git config --list --system
#### 清除配置
    git config --unset --local user.name
#### 使用git前的最小配置
git config --global user.name 'huoxiayu'
git config --global user.email 'huoxiayu@huoxiayu.com'

## 建立仓库
    已有项目（文件夹）纳入git管理 -> cd 项目目录 && git init
    直接新建一个被git管理的项目 -> git init 项目目录
    
## 建立裸仓库

    git init -bare
    裸仓库：一般将裸仓库用作共享仓库，每个人往里面push自己的修改同时pull别人的修改，裸仓库一般只有一个.git目录，而没有所谓的工作区，故一般的命名方式是用.git结尾（some_project.git）

## git状态流转图
![avatar](https://git-scm.com/book/en/v2/images/lifecycle.png)

    在被git管理的目录中新建文件时，这些文件处理"未被跟踪"的状态。
    已跟踪的文件指的是被纳入了版本控制的文件，在上一次的快照中有他们的记录。
    git status用于查看文件处于的状态
    git add .用于将将新文件纳入"跟踪"态或者将修改添加到暂存区
    git commit -m "commit message"用于将暂存区的修改提交到版本库
    .gitignore文件中可以添加不需要纳入git管理的文件类型，如编译产生的临时文件

## 理解工作区和暂存区
![avatar](https://static.liaoxuefeng.com/files/attachments/919020037470528/0)

## 版本库 .git
隐藏目录.git就是git的版本库
版本库中最重要的有：
    
    stage（或index）即暂存区，
    git自动创建的第一个分支master
    指针HEAD（HEAD在.git目录中其实是一个引用，指向当前分支或commit（如分离头指针的场景））
    config（其实就是git config --list --local看到的内容）
    refs（refs中有heads和tags）

## 文件重命名
推荐使用git mv old new不会破坏git的变更历史

## 清除暂存区
git reset --hard

## 查看修改
git diff 显示修改之后没有暂存起来的变化内容
git diff --staged 显示的是下一次commit时会提交到HEAD的内容(即暂存起来的变化内容)

## 查看版本演变历史
    git log
    git log --oneline 更简洁
    git log -n 5 查看最近5次修改
    git log --all --graph 以图形化方式展现所有分支的历史
    git log --oneline --all -n 4

## git内部原理
git的本质其实是一个简单的键值数据库   
你可以像git中插入任意类型的内容，git会返回一个唯一的键，通过该键可以获取到该内容
git cat-file -p master^{tree}

## commit、tree、blob的关系

    commit存储一次提交的信息，包含tree、parent_commit、author、commit_msg等
    tree相当于文件系统中的目录，记录了目录下文件的名字和hash的映射
    blob相当于是文件，因为文件名存储在tree中，所以多个内容相同的文件对应的是一个blob
    tag可看作是commit的别名
    执行git add将文件放入暂存区时会生成blob或者tree，多次git add不同的文件内容会生成多个blob
    执行git commit时会生成commit和tree（指向本次所有修改的目录）
    
## 查看git中的对象
git cat-file -t hash 查看对象类型
git cat-file -p hash 查看对象内容

## 分支的本质
分支的本质其实就是一个commit对象，不同的分支指向了不同的commit

## 分离头指针
    分离头指针：git checkout commit-hash
    git的提示：您正处于分离头指针状态。您可以查看、做试验性的修改及提交，并且您可以在切换回一个分支时，丢弃在此状态下所做的提交而不对分支造成影响。
    即git建议所有修改在分支上进行
    分离头指针的场景主要是用于实验性质的修改，如果最后实验发现不需要commit则可以直接丢弃掉