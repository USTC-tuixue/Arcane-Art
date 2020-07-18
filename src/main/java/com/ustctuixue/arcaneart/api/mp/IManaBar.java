package com.ustctuixue.arcaneart.api.mp;


public interface IManaBar
{
    /**
     * @return 鑾峰彇鐢熺墿褰撳墠 MP 鍊�
     */
    double getMana();

    /**
     * 璁剧疆鐢熺墿褰撳墠鐨� MP 鍊�
     * @param mana 璁剧疆鐨勫�硷紝濡傛灉灏忎簬 0 鍒欒涓烘槸 0
     */
    void setMana(double mana);


    /**
     * 娑堣�� MP 鍊�
     * @param mana 娑堣�楃殑 MP 閲�
     * @return 
     */
    boolean consumeMana(double mana);

    /**
     *
     * @return 杩斿洖 MP 鍥炲鍐峰嵈鏃堕棿锛屽崟浣嶄负 tick
     */
    int getRegenCoolDown();

    /**
     * 璁剧疆 MP 鍥炲鍐峰嵈鏃堕棿
     * @param coolDown 鍗曚綅涓� tick
     */
    void setRegenCoolDown(int coolDown);

    /**
     * 姣� tick 璋冪敤涓�娆★紝鐢ㄤ簬鍥炲鍐峰嵈鍊掕鏃�
     * @return 鏄惁鍙互寮�濮嬪洖澶�
     */
    boolean coolDown();


    /**
     * 鑾峰彇褰撳墠榄旀硶缁忛獙鍊硷紝鍗囩骇鏃舵竻闆�
     * @return 榄旀硶缁忛獙
     */
    double getMagicExperience();

    /**
     * 璁剧疆褰撳墠鐨勯瓟娉曠粡楠屽��
     * @param exp 缁忛獙鍊�
     */
    void setMagicExperience(double exp);

    /**
     * 澧炲姞榄旀硶缁忛獙鍊�
     * @param exp 缁忛獙鍊�
     */
    void addMagicExperience(double exp);

    /**
     * 鑾峰緱褰撳墠鐨勯瓟娉曠瓑绾�
     * @return 榄旀硶绛夌骇
     */
    int getMagicLevel();

    /**
     * 璁剧疆榄旀硶绛夌骇
     * @param level 绛夌骇
     */
    void setMagicLevel(int level);
}