# API 说明

## MP 系统

`package com.ustctuixue.arcaneart.api.mp;`

### MP 槽
生物具有 MP 槽，这里用 `IManaBar` 表示。
MP 槽中的 MP 余量可以用 `IManaBar::getMana()`获取，
可以用 `IManaBar::setMana(double)` 设置，可以用 
`IManaBar::consumeMana(double)` 来消耗 MP 槽，
参数为消耗量。

### 魔法经验和魔法等级

魔法经验和魔法等级整合到了 `IManaBar` 里面，
`MagicExperience` 代表魔法经验，
`MagicLevel` 代表魔法等级，
两者都有 getter 和 setter。
增加魔法经验时，可以用 `addMagicExperience(double)`。

### MP 上限
MP 上限可以通过生物的 Attribute 系统获取，
其对应的属性项为 `CapabilityMP::MAX_MANA`

例如，下面这个函数就可以获取生物的最大 MP
```java
public class Test{
    public double getMaxMP(LivingEntity entity)
    {
        return entity.getAttribute(CapabilityMP.MAX_MANA).getValue();
    }
}
```

### MP 回复速度
和 MP 上限一样，每个生物的 MP 回复速度是通过 Attribute 系统获取和更改的。
对应的属性项为：`CapabilityMP::REGEN_RATE`

### MP 回复计时器

整合到 MP 槽了，这里主要是 API 内部调用，故不作详细说明。

### MP 储存能力

能力及其 Storage 和默认的 Provider 储存在 `mp.tile.CapabilityStorage` 中。
能力的默认实现方式为 `MPStorage`。

## MP 相关事件

MP 槽相关的事件都是 `mp.MPEvent` 的子类，目前这一部分还没完成。

## 法术解释器

- 法术属性
  - 法术作用阶段
    - 按住右键时作用
    - 松开右键时作用


### 法术翻译器

将自然语言的单词翻译成机器语言的关键词（`ResourceLocation`的格式）。支持多语种，以及扩充关键词。

#### 注册关键词

关键词的基类：`spell.SpellKeyWord`，注册方法和注册方块、物品等如出一辙。
关键词分两种：可执行、不可执行
- 可执行：放在句首，用于引导一条完整的语句
- 不可执行：不能够放在句首，一般在语句中间用于引导从句或作为选项

SpellKeyWord 在构造时需要一个执行时机类型：
- COMMON：在任何时候都会执行
- ON_HOLD：在蓄力时会执行
- ON_RELEASE：在蓄力结束后释放时会执行
- NOT_EXECUTABLE：不可执行的关键词

#### 添加默认语言
使用 `LanguageManager.getLanguageProfile(String name)` 添加一个默认语言档案。

`name` 是语言的标识符。
该函数会返回添加的语言档案；语言档案并不会被重复添加：如果该档案已存在，则返回已存在的档案。

#### 为语言添加默认翻译
使用 `LanguageProfile::addTranslationFor(SpellKeyWord keyword, String translation)` 和
`LanguageProfile::setTranslationFor(SpellKeyWord keyword, String translation)` 为关键词添加该语言的默认翻译。

翻译需要在 ServerStartingEvent 击发之前添加。这是因为法术的语言配置文件会在这时生成。

#### 添加、修改法术语言

直接在 config/Incantation Languages 里面添加、修改语言设置文件即可。
empty.toml 是生成的语言文件模板，不会被读取。

### 添加法术

翻译器使用了 Mojang 的 Brigadier 库，以命令的形式注册并执行法术。
添加法术需要侦听 `SpellDispatcher.NewSpellEvent` 事件，用 `NewSpellEvent::getDispatcher` 获取 dispatcher 注册法术（指令）。

### 使用 API 内置的参数类型

法术的参数类型和 MC 的指令系统中参数的使用方式相同。但多了支持 “变量” 的特性。
这些参数类型存在于 `spell.interpreter.argument` 中。

####

