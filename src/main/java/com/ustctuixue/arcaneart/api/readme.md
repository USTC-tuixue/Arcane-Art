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

## MP 相关事件

MP 槽相关的事件都是 `mp.MPEvent` 的子类，目前这一部分还没完成。



## 法术解释器

- 法术属性
  - 法术作用阶段
    - 按住右键时作用
    - 松开右键时作用
    - 法球 impact 时作用
    - 陷阱触发时作用
  - 魔法消耗类型
    - 持续性：rate
    - 固定性：sum
  - 