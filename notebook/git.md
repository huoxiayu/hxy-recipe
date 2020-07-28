## git官网
https://git-scm.com/book/zh/v2

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

## 版本库
隐藏目录.git就是git的版本库
版本库中最重要的有：stage（或index）即暂存区，git自动创建的第一个分支master，以及指针HEAD

## 文件重命名
推荐使用git mv old new不会破坏git的变更历史

## 清除暂存区
git reset --hard

## 查看修改
git diff 显示修改之后没有暂存起来的变化内容
git diff --staged 显示的是下一次commit时会提交到HEAD的内容(即暂存起来的变化内容)

