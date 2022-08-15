package cn.com.pfinfo.weixin.websdk.mp.api.mp.draft;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.WxWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.ArticleModel;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.model.DraftModel;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by cuitpanfei on 2022/08/10
 *
 * @author cuitpanfei
 */
class WxMpWebDraftServiceTest {
    WxMpWebDraftService draftService = WxWebService.me().mpWebService().draftService();

    DraftModel draft = new DraftModel();

    @BeforeEach
    void setUp() {
        WxWebHttpUtil.getNewWxUin();
        MpApp.updateAppId("asd");
        MpApp.setToken("541941378");
        MpApp.updateCookie("appmsglist_action_3895151075=card; ua_id=wrkIJwT6jVoKslDGAAAAALKpokWAlfb2vNhPNFgTYzE=; mm_lang=zh_CN; pgv_pvid=4368920818; iip=0; pac_uid=0_f871b711bd216; wxuin=47583230377848; fqm_pvqid=15fdc9a7-dd1a-4393-8e72-4b92e8bcc388; noticeLoginFlag=1; uuid=67a54392fc7e9fe97cb761e66d251fa5; rand_info=CAESIMu7iA9xdUeLHrkdsBd0aEIi6hHSN0NY4/8q8SUair4U; slave_bizuin=3895151075; data_bizuin=3895151075; bizuin=3895151075; data_ticket=J6tgOdNpBdKK1WiPvt7rSIv2szt9uuTUUXlHtUh9JPGghIjzlMeOqESqUQk06kVj; slave_sid=Uk0wVzhqRFA1WWNrTDdVclpTWU5FejVLcGt1dEE4VUhrcThrM1pCblk4TG5ZM1pIcEdSZ09wakhDN0x0M1RQS25IN3BwTk5Za0xzQ2xfS1lMRVU3RXRJS1JCUmk3XzQxbVloY2JITUI2WF9RXzl3dFBYdEdUUmhlclp2S2V0OTRqTjVpMWg0dmFHV2NuQ2JG; slave_user=gh_2b50a79cdb27; xid=2089fe13635ec72e64834ef131c95707; rewardsn=; wxtokenkey=777");

        draft.setAuthor("飞羽英华");
        List<ArticleModel> articles = new ArrayList<>();
        articles.add(ArticleModel.builder().article("<section class=\"mp_profile_iframe_wrp\"></section>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">一、新政后哪些提取业务可以按月提取？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可以按月提取的业务有购买自住住房提取、偿还自住住房贷款本息提取、租赁自住住房提取三类。购买自住住 房提取调整为&ldquo;购房五年内申请，可每月提取一次&rdquo;；偿还自住住房贷款本息提取调整为&ldquo;可每月提取一次&rdquo;，其中，提前结清贷款提取调整为&ldquo;提前结清贷款后五年内申请，可 每月提取一次&rdquo;；租赁自住住房提取调整为&ldquo;可每月提取一次&rdquo;。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">二、按月提取的业务可以通过哪些渠道办理？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可通过成都公积金 APP 线上渠道和中心线下服务窗口办理，其中，职工还可以跟中心签订按月提取协议，每月 自动提取账户余额用于偿还自住住房贷款本息。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">三、按月提取的业务是否需要每月都提出申请？可不可以累计提取？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可以累计提取。职工可根据自己需要，确定申请提 取的间隔时间，无需每月都提出申请。每次申请提取时，中 心将根据未申请提取的月数计算职工应提取的金额。需要注意的是，无备案租赁自住住房提取不能跨年累计提取。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">例如，职工李某于 2021 年 10 月按揭购买了一套商品住房，于 11 月申请了首次偿还自住住房贷款本息提取，其后一直未申请提取，直到 2022 年 5 月，李某再次就该套住房贷款申请偿还自住住房贷款本息提取，其可将未申请提取的2021 年 12 月至 2022 年 5 月间的还款额，累计在一起申请提取。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">四、哪些提取业务同一个年度只能选择一套住房申请提 取？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：购买、建造、翻建、大修自住住房、偿还自住住房贷款本息、租赁自住住房这几类住房消费提取业务，职工在同一个年度内，只能选择一套住房进行提取。需要注意的是， 既有住宅增设电梯提取不受此限制，例如职工当年已办理了购买自住住房或偿还自住住房贷款本息提取的，还可以办理既有住宅增设电梯提取。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">五、可用于提取的余额是指什么？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可用于提取的余额是指职工申请提取时的账户余 额，但不包含因纳入住房公积金贷款额度计算、司法部门冻 结、职工个人未办结公积金业务等原因锁定在账户中的余额。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">六、纳入贷款额度计算的缴存余额怎么提取？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：职工申请公积金贷款时，纳入贷款额度计算的缴存 余额，自公积金贷款受理之日起至贷款发放前，不得申请提 取；在公积金贷款发放后至贷款结清前，仅能用于偿还该套 住房的公积金贷款本息提取。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">七、新政后能否以公积金贷款所购的住房申请提取首付款？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可以。职工在 2021 年 10 月 8 日后申请办理的公积金贷款，可提取未纳入贷款额度计算的缴存余额用于支付该 套住房首付款。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">例如，张三账户余额 8 万元，2021 年 10 月 20 日申请住房公积金贷款，其公积金账户余额中有 3 万元用于计算贷款额度，另外的 5 万元未用于计算贷款额度。因此，在贷款发放后，张三可申请将未纳入贷款额度计算的 5 万元账户余额提取用于支付该套住房的首付款。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">八、异地房屋申请提取有什么变化？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：2021 年 10 月 8 日后，将原有&ldquo;职工及其配偶本市无房，在川内购房申请提取住房公积金，不受户籍地和工作 地限制&rdquo;的规定，调整为以德阳市、眉山市、资阳市行政区 域内的住房申请提取公积金，将不受户籍所在地或工作所在 地限制。职工购买德阳市、眉山市、资阳市行政区域以外住 房的提取时，房屋地址应与职工本人或配偶户籍所在地或工 作所在地的地级行政区域一致。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">九、新政前已经以异地房屋办理过提取的，新政后，是 否需要重新提交户籍地或工作所在地证明？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：已按原提取政策办理过异地房屋的购买自住住房提 取或偿还自住住房贷款本息提取业务的职工，在 2021 年 10月 8 日后再次以同一套异地房屋申请提取的，视为符合户籍地或工作所在地的提取条件，无需重新提交户籍地或工作所在地证明。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十、限制多套房提取是什么？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：自 2021 年 10 月 8 日起，职工申请时近五年内已办理两套及以上住房购房提取，或当前在本市拥有两套及以上 住房产权的，不得再申请第三套及以上住房购房提取。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十一、限制多套房提取具体如何理解？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：一是限制多套房提取的规定，仅针对通过按揭或全 款购买新房以及再交易住房的购买自住住房提取，不包含偿 还自住住房贷款本息提取等其他提取业务。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">二是近五年内已办理的购买自住住房提取套数，是以中 心业务系统提取明细中的记载为依据。需要注意的是，2021 年 10 月 8 日以前产生的系统提取记录不纳入购买自住住房提取套数的认定范围。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">例如，职工张某名下有两套住房，分别于 2018 年 9 月、2021 年 11 月办理了这两套住房的购买自住住房提取业务，由于 2021 年 10 月 8 日以前的购买自住住房提取记录不纳入提取套数的认定范围，那么张某五年内仅有一套购买自住住 房提取记录。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">三是本市拥有的房屋套数以住建部门登记的房屋套数 及取得时间进行排序，排序为第三套及以后的住房不能办理 购买自住住房提取，第一套和第二套符合提取条件的，可以 申请购买自住住房提取。</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">例如，职工王某当前在本市有 A、B、C 三套住房，分别购于 2011 年、2017 年、2021 年，按照上述规定，C 住房为第三套住房，不能申请购买自住住房提取，A 住房由于购房时间超过了五年，也不能申请购买自住住房提取，仅有 B 住房不属于第三套及以上的住房，且购房时间也未超过五 年，可以申请购买自住住房提取。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十二、新政后精简了哪些提取业务材料？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：一是已婚职工办理本人名下房屋的住房消费类提取（不含租赁自住住房提取），无需再提供婚姻证明材料；二是办理购本市商品房提取不再提供购房合同；三是办理偿还自住住房贷款本息提取不再提供购房合同或不动产权证；四是办理低保提取不再提供低保证明；五是办理退休提取不再提供退休证明；六是办理既有住宅增设电梯提取不再提供电梯竣工验收报告；七是第一顺序继承人（配偶、父母、子女） 提取死亡职工公积金，不再提供继承权或受遗赠权公证书。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十三、什么情况下需要提供婚姻关系证明材料？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：无房屋所有权，但属于房屋所有权人配偶的职工申 请办理购买、建造、翻建、大修自住住房、偿还自住住房贷 款本息、既有住宅增设电梯提取业务的，或已婚职工申请办 理租赁自住住房提取业务的，需要提供婚姻关系证明材料。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十四、提取死亡职工公积金跟以前比有什么区别？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：提取死亡职工公积金时，第一顺序继承人（配偶、 父母、子女）可以签署承诺书方式替代提供继承公证书；第 一顺序继承人以外的提取人，仍需提供继承权或者受遗赠权的公证书或法院的判决书、裁定书、调解书。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十五、职工不能在线上办理提取业务，也无法亲自到柜 台办理的，是否可以委托他人代办？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：可以。职工可委托他人办理除租赁自住住房提取、 购买再交易住房提取以外的提取业务。委托方式包含通过手 机 APP 办理委托登记，或填写纸质《成都住房公积金提取委托代办书》交由代办人代办业务时提供。</p>\n" +
                "<h2 style=\"text-align: center; color: #3f3f3f; line-height: 1.5; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 140%; margin: 80px 10px 40px 10px; font-weight: normal;\">十六、新旧政策在衔接过渡方面有何具体的规定？</h2>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">答：有四个方面的衔接过渡规定： 一是 10 月 8 日前受理的公积金贷款不能再以该套住房办理购买自住住房提取； 二是 10 月 8 日前已办理过购买自住住房提取的职工，不能再次以该套住房办理购买自住住房提取； 三是 10 月 8 日前以提前结清贷款办理过偿还自住住房贷款本息提取的职工，不能再次以该套住房办理偿还自住住房贷款本息提取； 四是 10 月 8 日后，不再新增签订&ldquo;按年委托提取协议&rdquo;，10 月 8 日前职工已签订且未解约的&ldquo;按年委托提取协议&rdquo;，按协议约 定继续执行。</p>\n" +
                "<hr style=\"border-style: solid; border-width: 1px 0 0; border-color: rgba(0,0,0,0.1); -webkit-transform-origin: 0 0; -webkit-transform: scale(1, 0.5); transform-origin: 0 0; transform: scale(1, 0.5);\" />\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">解读机构：成都住房公积金管理中心归集业务管理部</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">解读人：周执锐</p>\n" +
                "<p style=\"text-align: left; color: #3f3f3f; line-height: 1.6; font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif; font-size: 16px; margin: 10px 10px;\">联系方式：（028）12329 （028）86279457</p>")
                .title("成都住房公积金提取管理实施细则 - 解读2")
                .author("飞羽英华").summary("《成都住房公积金提取管理实施细则》解读").build());
        draft.setArticles(articles);
    }


    @Test
    void createDraft() {
        draftService.createOrUpdateDraft(draft);
    }

    @Test
    void deleteDraft() throws ScriptException {
        draftService.deleteDraft(100000353L);
    }


    @Test
    void queryDrafts() {
        draftService.queryDrafts(0, 10);
    }
}