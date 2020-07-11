## 编写法术

法术由指令组成，一句话（以句号结尾）为一条指令。

一个法术可以由多个指令组成。

法术复杂度与法术字符长度线性相关。

法术所用语言需要符合下列条件之一：

- 古老的语言：大部分使用这门语言的人可以直接理解 1200 年前用该语言书写的文献
- 小众的语言：使用该语言的人数全球占比 < 0.5%
- 联合国工作语言

单词的格与性不做区分。

一共有三种量：实体、位置、数量

将实体传入一个位置参数时，会将其位置传入语句

### 可用指令

启动式

- Place: 放置方块、陷阱

  - place \<类型> \[at 从句] 
  - place trap <陷阱效果> [at 从句]
  - 类型：
    - block：放置快捷栏中施法手套右边的方块到 at 从句指定的位置，若没有 at 从句，则放置到自身所在位置
    - luminator：放置光源
    - virtual-block：虚拟方块
  - 陷阱效果：

- summon：召唤物品、实体、法球

  - summon <实体名> [at 从句]
  - summon <物品名>
  - summon <法球名> \[towards 从句]

- effect：给实体增加 buff / debuff

  - effect \<buff> with power of \<强度> and duration of <时间> on <实体选择器>

  - 可用 buff 列表：

  - | buff     | 描述 | 基础消耗 |
    | -------- | ---- | -------- |
    | 生命回复 |      | 100      |
    | 夜视     |      | 10       |
    | 抗火     |      | 60       |
    | 水肺     |      | 60       |
    | 速度     |      | 40       |
    | 跳跃提升 |      | 60       |
    | 凋零     |      | 150      |
    | 失明     |      | 150      |

    

- enchant：给物品临时附魔

  - enchant \<sword | helmet | chestplate | leggings | boots | pickaxe | axe | bow | hoe | shovel> \<附魔> <等级> <时长>

- grab：移动实体、方块

  - grab <实体选择器|block [at 从句]> with max power of <数字> \<hold | accelerate towards 从句>
  - grab 的魔耗和实体的碰撞箱体积正相关，和方块硬度成正相关
  - 用 with max power of 选项限制能抓握的最大物体硬度和碰撞箱体积
  - hold 表示消除重力
  - towards 从句表示抓住后加速的方向



处理语句

- Let 用于设置一个常量
  - 语法：let <变量名> be <位置 | 实体选择器>
- shift 语句：
  - shift by \<x> \<y> \<z>
  - 对 shift 语句前面的位置量进行平移操作



选择语句

- 位置从句：表示一个位置，可以是实体或者位置，一般用 at 来引导，有 at 引导时又称 at 从句
  - at \<x> \<y> \<z>：相对于法球的坐标
  - at <实体选择器>：此时返回实体所在的位置
- Towards 从句：设置一个方向，后面可以接实体或相对位置
  - towards \<实体选择器>
  - towards \<x> \<y> \<z>
  - 用于获取从施法位置指向目标位置 | 实体的向量
- 实体选择器：用于选择一个或多个实体，只用于需要出现实体选择器的情况
  - 实体类别： ally | enemy | entity | item | passive | player 
  - nearest \[n] \<实体类别> to \<位置从句>：获取离某位置最近的 n 个实体
  - me ：指代发出法术的实体
  - <实体类别 | position> <实体选择器> \<合适的 be 动词> looking at：获取实体选择器视线所指的实体 | 位置