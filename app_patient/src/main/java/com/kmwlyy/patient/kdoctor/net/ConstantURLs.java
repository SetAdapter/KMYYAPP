package com.kmwlyy.patient.kdoctor.net;

import com.kmwlyy.patient.kdoctor.net.BaseConstants;

/**
 * 项目名称：trunk
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/6/7 17:08
 * 修改人：Administrator
 * 修改时间：2016/6/7 17:08
 * 修改备注：
 */
public class ConstantURLs {
    /**
     * 获取个人信息
     */
    public static final String GET_USERINFO = BaseConstants.SERVER_URL + "api/Patient/Info";

    /**
     * 收藏医生/医院/资讯/动态/关心(又名点赞)
     * OBJ_TYPE	int 1.医生,2.医院,3.资讯,4.动态,5.关心 ，6.课程
     * OBJ_ID	String 被收藏，被关心对象的ID
     */
    public static final String ADDCOLLECTLINK = "api/CollectLink/AddCollectLink";
    /**
     * 取消医生/医院/资讯/动态/关心(又名点赞)
     * OBJ_TYPE	int 1.医生,2.医院,3.资讯,4.动态,5.关心，6.课程
     * OBJ_ID	String 被收藏，被关心对象的ID
     */
    public static final String CANELCOLLECTLINK = "api/CollectLink/CanelCollectLink";

    /*获取所有收藏课程*/
    public static final String GET_COLLECT_WITH_COURSE = "/api/CollectLink/GetCollectWithCourse?";
    /*获取所有收藏的资讯*/
    public static final String GET_COLLECT_WITH_NEWS = "/api/CollectLink/GetCollectWithNews?";
    /*获取所有收藏的医生*/
    public static final String GET_COLLECT_WITH_DOCTOR = "/api/CollectLink/GetCollectWithDoctor?";
    /*获取所有收藏的医院*/
    public static final String GET_COLLECT_WITH_HOSPITAL = "/api/CollectLink/GetCollectWithHospital?";

    /*获取网络医院的医生排班*/
    public static final String GET_SCHEDULE = "api/NetworkMedical/GetSchedule?";

    /*获取科室列表*/
    public static final String GET_DEPARTMENT_LIST = "/api/HospitalExternal/GetDepartmentList?";
    /*获取科室对应的医生列表*/
    public static final String GET_DOCTOR_LIST = "/api/DoctorExternal/GetDoctorList?";

    /*修改保存收货人信息*/
    public static final String SAVE_RECEIPT_ADDRESS = "/api/ReceiptAddress/SaveReceiptAddress";
    /*删除收货人信息*/
    public static final String DELETE_ADDRESS = "/api/ReceiptAddress/DeleteAddress?id=";
    /*获取登录用户的收货地址信息*/
    public static final String GET_RECEIPT_ADDRESS = "/api/ReceiptAddress/GetReceiptAddress";
    /*设置默认收货地址*/
    public static final String UPDATE_DEFAULT_ADDRESS = "/api/ReceiptAddress/UpdateDefaultAddress?id=";


    /*获取所有自测列表*/
    public static final String GET_MY_TEST_LIST = "/api/Evaluating/GetMyEvaluationTemplateResultsList";
    /*提交反馈记录*/
    public static final String INSERT_FEED_BACK = "/api/FeedBack/InsertFeedBack";

    /*anychat咨询记录*/
    public static final String UPDATE_CONSULT_LOG = "/api/OnlineConsult/UpdateConsultLog";// post  参数包PayOrderParamDTO{ ConsultID=14 }  更新咨询开始时间

    /*获取图文咨询列表*/
    public static final String GET_IM_LOGLIST = "api/OnlineConsult/GetInfoConsultRecordList?pageSize=10&pageIndex=";

    /*获取医生的详情*/
    public static final String GET_DOCTOR_DETAIL = "api/NetworkMedical/GetDoctorsByDepartment";
    /*获取反馈栏目列表*/
    public static final String GET_MENUS = "/api/FeedBack/GetMenus";
    /*接受医生的请求*/
    public static final String ACCEPT_DOC = "/api/OnlineConsult/UpdateConsultLog";
    /*首页，获取消息*/
    public static final String GETNOTICE_MESSAGE = "api/Common/GetNoticeMessage";
    /*首页，获取banner图*/
    public static final String GET_HOMEBANNER_SLIDESHOW = "api/GetCarouselList";
    /*发送视频聊天记录*/
    public static final String CHAT_RECORD = "api/OnlineConsult/InsertAndroidConsultChatRecord";
    /*退出登录*/
    public static final String LOGOUT = "/api/Account/Logout";
    /*通过参数找医生*/
    public static final String FIND_DOCTORS = "api/NetworkMedical/GetDoctors";
    /*通过参数找医生*/
    public static final String FIND_DOCTORS_BY_DEPART = "api/NetworkMedical/GetDoctorsByDepartment";
    /*获取新闻数据*/
    public static final String NEWS_SEARCH = "/api/News/Search";
    /*获取评测数据*/
    public static final String EVALUATING_SEARCH = "/api/Evaluating/Search";


    /*疾病详情*/
    public static final String DISEASE_DETAIL = "/api/Disease/GetDiseaseDetails";

    /*搜索全部*/
    public static final String SEARCH_ALL = "elasticsearch/searchapp/advancedqueryforapp";

    /*分类搜索*/
    public static final String SEARCH_TYPE = "elasticsearch/commonsearch/advancedquerywithtypes";
    /*分类搜索*/
    public static final String SNSSEARCH = "elasticsearch/searchapp/snsSearch";

    /*关键词联想*/
    public static final String WORD_THINK = "elasticsearch/commonsearch/querykeyword";
    /*科室列表*/
    public static final String DEPARTMENT_LIST = "/api/NetworkMedical/GetPubDepartmentList";
    /*科室热门医生*/
    public static final String RECOMMEND_DEPARTMENT_DOCTORS = "/api/Doctor/GetRecommendDepartmentDoctors";
    /*已咨询过的医生列表*/
    public static final String CONSULTED_DOCTORS = "/api/NetworkMedical/GetMyVisitDoctors";
    /*今日义诊*/
    public static final String FREE_CONSULTED_DOCTORS = "/api/NetworkMedical/GetFreeClinicDoctors?pageIndex=1&pageSize=10&date=null";

    /**
     * 判断用户是否收藏1.医生 2.医院 3.新闻 4.动态,6课程
     */
    public static final String GET_COLLECT_STATE = "/api/CollectLink/IsCollectLink?";

    /**
     * 购买图文咨询的接口
     */
    public static final String BUY_IMAGE_TEXT = "/api/NetworkMedical/InsertUserConsults";

    /**
     * 购买语音视频预约的接口
     */
    public static final String BUY_ORDER = "/api/NetworkMedical/InsertUserOPDRegister";
    /**
     * 修改咨询订单的接口
     */
    public static final String UPDATECONSULTORDER  = "api/order/UpdateConsultOrder";

    /**
     * 是否可以进入诊室
     */
    public static final String CANENTERROOM  = "api/order/CanEnterRoom";

    /**
     * 修改咨询订单的接口
     */
    public static final String GETCHANNELKEY  = "/api/Doctor/GetChannelKey";

    /**
     * 获取图文咨询订单列表
     */
    public static final String TUWEN_ORDER = "/api/NetworkMedical/GetUserConsults?CurrentPage=";

    /**
     * 获取预约咨询订单列表
     */
    public static final String YUYUE_ORDER = "/api/NetworkMedical/GetUserOPDRegister";
    /**
     * 申请退款接口
     */
    public static final String APPLY_FOR_REFUND = "/api/NetworkMedical/Refund?OrderNo=";//订单号：DH2016100813595898340868
    /**
     * 取消订单
     */
    public static final String CANCEL_ORDER = "/api/NetworkMedical/Cancel?OrderNo=";
    public static final String GET_KM_CONFIG = "/api/NetworkMedical/GetKmApiconfig";
    /**
     * 发起预约挂号
     */
    public static final String SET_YUYUE = "api/AppointmentDoctor/SetYuyue";
    /**
     * 取消预约挂号
     */
    public static final String CANCEL_REGISTER = "api/AppointmentDoctor/CancelRegister";

    /**
     * 获取智能回复列表
     */
    public static final String AUTOANSWERLIST = "elasticsearch/autoanswer/autoAnswerList";
    /**
     * 是否存在患者信息
     */
    public static final String ISEXIST_PATIENT_INFO = "/api/ChineseMedicine/IsExistPatientInfo";
    /**
     * 首次进入国医馆
     */
    public static final String FIRST_ENTER_MEDICAL_MUSEUM = "/app/GygHtmlIndex";
    /**
     * 判断默认就诊人
     */
    public static final String IS_DEFAULT_PATIENT = "/api/NetworkMedical/GetDefaultUserMembers";
    /**
     * 获取消息中心所有消息
     */
    public static final String GET_ALL_MESSAGELIST = "api/MessagePush/GetAllMessageLst?pageIndex=0&pageSize=10";
    /**
     * 清楚消息中心所有消息
     */
    public static final String CLEAR_ALL_MSG = "api/MessagePush/ClearAllMsg";
    /**
     * 将消息中心所有未读消息标记为已读
     */
    public static final String SING_ALL_MSG_READED = "api/MessagePush/SetIsRead";
    /**
     * 获取所有课程列表
     */
    public static final String GET_COURSE_ALL_LIST = "/api/TrainingTeacher/GetCourseList";
    /**
     * 获取课程详情
     */
    public static final String GET_COURSEDETAIL = "/api/trainingteacher/getcourse";
    /**
     * 获取云通信配置信息
     */
    public static final String GET_IM_CONFIG = "/api/Account/GetIMConfig";
    /**
     * 获取四个主题课程
     */
    public static final String GET_STUDY_THEME = "/api/trainingteacher/coursecategory";
    /**
     * 病症详情
     */
    public static final String GET_SYMPTOM_DETAIL = "/api/Symptom/GetSymptomDetail?ID=";
    /**
     * 康博士回复信息
     */
    public static final String K_DOCTOR_REPLY = "elasticsearch/DrKang/chatWithDrKang";
    /*获取首页上的人动态*/
    public static final String GET_HOME_HOTPOSTS = "api/dynamic/GetHotPostList?pageIndex=0&pageSize=2";
    /*获取热门动态下的回复*/
    public static final String GET_HOTPOSTS_REPLY = "api/DynamicLoop/GetDynamicLoopReplyList";
    /*获取健康关注评测题目列表*/
    public static final String GETSUBJECTLST = "/api/trainingteacher/GetSubjectLst";
    /*获取健康关注评测结果*/
    public static final String GETRESULT = "/api/trainingteacher/GetResult";
    /*获取方案详情*/
    public static final String GETPROGRAMMEDELTAILS = "/api/trainingteacher/GetProgrammeDeltails";
    /*加入方案*/
    public static final String INSERTPROGRAMME = "api/trainingteacher/InsertProgramme";
    /*添加打卡*/
    public static final String INSERTCLOCKIN = "/api/trainingteacher/InsertClockIn";
    /*打卡记录列表*/
    public static final String GET_CLOCKIN_LIST = "/api/trainingteacher/GetClockInList";
    /*添加打卡*/
    public static final String GET_RECOMMENDEDVIDEO_LIST = "/api/trainingteacher/GetRecommendedVideoList";
    /*编辑子方案*/
    public static final String EDITSONPROGRAMME = "/api/trainingteacher/EditSonProgramme";
    /*删除子方案*/
    public static final String DELSONPROGRAMME = "/api/trainingteacher/DelSonProgramme";
    /*TrainingTeacher - 是否开启闹钟*/
    public static final String ISALARMCLOCK = "/api/trainingteacher/IsAlarmClock";
    /*获取热门话题列表*/
    public static final String HOT_TOPIC = "api/dynamic/GetTopicList";
    /*获取热门内容列表*/
    public static final String HOT_CONTENT = "api/dynamic/GetPostList";
    /*根据话题ID获取下面的所有帖子*/
    public static final String GETTOPICDETAILPOSTLIST = "api/dynamic/GetTopicDetailPostList";
    /*获取热门内容列表*/
    public static final String GETMYPOSTLIST = "api/dynamic/GetMyPostList";
    /*取消操作*/
    public static final String CANCELOPERATION = "api/dynamic/CancelOperation";
    /*执行操作*/
    public static final String EXECUTEOPERATION = "/api/dynamic/ExecuteOperation";
    /*发帖操作*/
    public static final String SUBMITDYNAMICPOST = "api/dynamic/SubmitDynamicPost";
    /*获取回复列表*/
    public static final String GETREPLYLIST = "api/dynamic/GetReplyList";
    /*提交回复*/
    public static final String SUBMITREPLY = "api/dynamic/SubmitReply";
    /*提交回复*/
    public static final String UPDATEREADNUM = "api/dynamic/UpdateReadNum";
    /*获取我自己关注过的话题-跟我一样*/
    public static final String GETMYTOPICLIST = "api/dynamic/GetMyTopicList";
    /*获取我自己关注过的话题-跟我一样*/
    public static final String GETLIKEMELIST = "api/dynamic/GetLikeMeList";
    /*获取跟我一样的帖子列表*/
    public static final String GETLIKEPOST = "api/dynamic/GetLikeMePostList";
    /*获取话题详情*/
    public static final String GETTOPICDETAIL = "api/dynamic/GetTopicDetail";
    /*获取帖子详情*/
    public static final String GETPOSTDETAIL = "api/dynamic/GetPostDetail";
    /*获取我/其他个人主页信息*/
    public static final String GETACCOUNTDETAIL = "api/dynamic/GetAccountDetail";
    /*获取我/其他人关注过的话题-个人主页*/
    public static final String GETMYOROTHERTOPICLIST = "api/dynamic/GetMyOrOtherTopicList";
    /*我关注的人*/
    public static final String GETMYFOLLOWUSER = "api/dynamic/GetMyFollowUser";
    /*我的粉丝*/
    public static final String GETFANS = "api/dynamic/GetFans";

    /*工作室的状态*/
    public static final String OFFICESTATES = "api/Doctor/GetDoctorStatusEntity";
    /*工作室的认证*/
    public static final String DOC_COMMITAUDITING = "api/Doctor/CommitAuditing";

    /*培训工作室_视频课程详情页*/
    public static final String GETDOCTORVIDEOCOURSEDETAIL = "api/Doctor/GetDoctorVideoCourseDetail";

    /*培训工作室_评论列表*/
    public static final String GETDOCTORREPLYLIST = "api/Doctor/GetDoctorReplyList";

    /*培训工作室_结束咨询订单*/
    public static final String CLOSECONSULTORDER = "api/order/CloseConsultOrder";

    /*培训工作室_订单评价*/
    public static final String EVALUATE = "api/OrderEvaluate/Evaluate";

    /*培训工作室_获取订单聊天室详情*/
    public static final String GETCHATROOM = "api/order/GetChatRoom";

    /*培训工作室_提交回复*/
    public static final String DoctorReplyCourse = "api/Doctor/DoctorReplyCourse";

    /*获取医生工作室排班信息*/
    public static final String SEARCHDOCTORSCHEDULE  = "api/Doctor/SearchDoctorSchedule";

    /*获取附近家庭医生*/
    public static final String GET_FAMILY_DOC_LIST = "api/FamilyDoctor/GetFamilyDoctorList";
    /*获取家庭医生详情*/
    public static  final String GET_FAMILY_DOC_DETAIL = "/api/FamilyDoctor/GetFamilyDoctorDetail";
    /*购买家庭医生服务*/
    public static final String BUY_FAMILY_DOC = "api/Order/BuyFamilyDoctorService";
    /*获取家庭医生个人信息*/
    public static  final String GET_OFFICE_DOCTOR_DETAIL = "api/Doctor/GetDoctorDetail";
    /*购买家庭医生后获取订单详情*/
    public static final String GET_ORDER_DETAIL_FAMILYDOC = "api/FamilyDoctor/GetFamilyDoctorOrderEntity";
    /*提交家庭医生订单*/
    public static final String CREATECONSULTORDER = "api/order/CreateConsultOrder";
    /*发送合同给医生*/
    public static final String SEND_AGREEMENT_TO_DOC = "api/Order/SendOrderNotice";
    /*获取医生*/
    public static final String SEARCH_DOC = "/api/doctor/search";

    /**
     * 获取工作室所有消息
     */
    public static final String GET_ALL_OFFICE_MESSAGELIST = "api/Doctor/GetMessageLst?pageIndex=0&pageSize=100";
    /* 家庭医生订单列表*/
    public static final String FAMILY_DOCTOR_ORDER_LIST = "api/Order/GetFamilyDoctorOrder";
    /* 家庭医生订单详情*/
    public static final String FAMILY_DOCTOR_ORDER_DETAIL = "api/Order/GetFamilyDoctor";
    /*获取医生评价*/
    public static final String GET_DOC_EVALUATE = "api/OrderEvaluate/GetEvaluateList";
    /*判断问诊码是否有效及被使用*/
    public static final String IS_DISCOUNT_CODE_EFFECTIVE = "api/NetworkMedical/JudgeCode";
    /*问诊码支付*/
    public static final String INTERROGATION_CODE_PAY = "api/NetworkMedical/SinglePayment";
    /* 疾病百科，症状百科详情 */
    public static final String DISEASE_SYMPTOM_UONION_URL = "elasticsearch/searchapp/searchByDiseaseAndSymptom";
    /*登录*/
    public static final String LOGIN = "api/NetworkMedical/Login";
}
