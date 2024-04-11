@Automation
@Equities
@ACPPAges
@ThreadLocalEditions
Feature: Equities: ACP Equities

  @tmsLink=CT-6971
    @CT-6971 @CoreRegression
  Scenario Outline: user sees relevant metadata for some companies that have country names in their names
    Given a SIGNED_OUT user on the <page> page in <edition> edition
    Then company title should be "<company_title>" and not stock
    Examples:
      | edition | page                                       | company_title                            |
      | www     | /equities/france-telecom                   | Orange SA (ORAN)                         |
      | uk      | /equities/france-telecom                   | Orange SA (ORAN)                         |
      | ca      | /equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
      | au      | /equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
      | ph      | /equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
      | ng      | /equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
      | de      | /equities/france-telecom                   | Orange SA (ORAN)                         |
#      | es      | /equities/portugal-telecom                 | Pharol SGPS SA (PHRA)                    |
#      | mx      | /equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |
#      | fr      | /equities/taiwan-semicond.manufacturing-co | Taiwan Semiconductor Manufacturing (TSM) |

  @tmsLink=CT-6973
    @CT-6973 @CoreRegression
  Scenario Outline: user sees relevant metadata for companies commentary that have country names in their names
    Given a SIGNED_OUT user on the <page> page in <edition> edition
    Then company title should be "<company_title>" and not stock
    Examples:
      | edition | page                                                  | company_title                            |
      | www     | /equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | uk      | /equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | ca      | /equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | au      | /equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | ph      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ng      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | de      | /equities/france-telecom-commentary                   | Orange SA (ORAN)                         |
      | es      | /equities/portugal-telecom-commentary                 | Pharol SGPS SA (PHRA)                    |
      | mx      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | fr      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | it      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | nl      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | pt      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | pl      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | br      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ru      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | tr      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | sa      | /equities/taiwan-semicond.manufacturing-co-commentary | (TSM) Taiwan Semicond.Manufacturing Co   |
      | vn      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | hi      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | ms      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | th      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor (TSM)               |
      | hk      | /equities/taiwan-semicond.manufacturing-co-commentary | 台積電 (TSM)                              |
      | id      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | kr      | /equities/taiwan-semicond.manufacturing-co-commentary | TSMC (TSM)                               |
      | cn      | /equities/taiwan-semicond.manufacturing-co-commentary | 台积电 (TSM)                              |
      | se      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | fi      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | il      | /equities/taiwan-semicond.manufacturing-co-commentary | (TSM) טייוואן סמיקונדקטור מניופקטורינג   |
      | gr      | /equities/taiwan-semicond.manufacturing-co-commentary | Taiwan Semiconductor Manufacturing (TSM) |
      | jp      | /equities/taiwan-semicond.manufacturing-co-commentary | 台湾セミコンダクター・マニュファクチャリング (TSM)|
