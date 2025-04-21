Feature: 企业复工申请资料提交表测试

  Scenario: 提交企业复工申请
    Given 打开企业复工申请资料提交表页面
    When 在第一页选择"连续生产/开工类企事业单位"并点击下一页
    Then 第一页截图保存为"page1_screenshot"

    When 在第二页填写申请信息并点击下一页
    Then 第二页截图保存为"page2_screenshot"

    When 在第三页填写备案信息并提交
    Then 第三页截图保存为"page3_screenshot"
    And 验证提交成功提示信息为"提交成功"
