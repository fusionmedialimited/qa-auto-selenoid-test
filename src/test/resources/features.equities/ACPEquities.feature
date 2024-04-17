@Automation
@Equities
@ACPPAges
@ThreadLocalEditions
Feature: Equities: ACP Equities

  @tmsLink=CT-6971
    @CT-6971 @CoreRegression
  Scenario Outline: user sees relevant metadata for some companies that have country names in their names
    Given user on the <page> page in <edition> edition
    When user closes Privacy popup
    Then company title should be "<company_title>" and not stock

    Examples:
      | edition | page                                                    | company_title                            |
      | www     | investing.com/equities/france-telecom                   | Orange SA (ORAN)                         |
      | uk      | investing.com/equities/france-telecom                   | Orange SA (ORAN)                         |
      | ca      | investing.com/equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
      | au      | investing.com/equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
      | ph      | investing.com/equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
      | ng      | investing.com/equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
      | de      | investing.com/equities/france-telecom                   | Orange SA (ORAN)                         |
      | es      | investing.com/equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
      | mx      | investing.com/equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
      | fr      | investing.com/equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |


  @tmsLink=CT-6973
    @CT-6973 @CoreRegression
  Scenario Outline: user sees relevant metadata for companies commentary that have country names in their names
    Given user on the <page> page in <edition> edition
    When user closes Privacy popup
    Then company title should be "<company_title>" and not stock

    Examples:
      | edition | page                                                               | company_title                            |
      | www     | investing.com/equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | uk      | investing.com/equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | ca      | investing.com/equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | au      | investing.com/equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | ph      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ng      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | de      | investing.com/equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | es      | investing.com/equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | mx      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | fr      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | it      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | nl      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | pt      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | pl      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | br      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ru      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | tr      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | sa      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | (TSM) Taiwan Semicond.Manufacturing Co   |
      | vn      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | hi      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ms      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | th      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor (TSM)               |
      | hk      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | 台積電 (TSM)                              |
      | id      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | kr      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | TSMC (TSM)                               |
      | cn      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | 台积电 (TSM)                              |
      | se      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | fi      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | il      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | (TSM) טייוואן סמיקונדקטור מניופקטורינג   |
      | gr      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | jp      | investing.com/equities/taiwan-semicond.manufacturing-co-commentary | 台湾セミコンダクター・マニュファクチャリング (TSM)|
