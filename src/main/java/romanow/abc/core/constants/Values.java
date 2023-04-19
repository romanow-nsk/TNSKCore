package romanow.abc.core.constants;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.*;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;
import romanow.abc.core.prepare.WeekCellList;

import java.util.HashMap;


public class Values extends ValuesBase {
    private static Values two=null;
    private Values(){
        super();
        System.out.println("Инициализация Values");
        }
    public final static Values init(){
        if (two == null){
            two = new Values();
            two.afterInit();
            setOne(two);
            }
        return two;
        }

    // 1. Константы наследуются (аннотации)
    // 2. Массивы строк перекрываются
    // 3. статическая инициализация наследуется
    private final static int EMReleaseNumber=1;                  // номер сборки сервера
    private User superUser = new User(UserSuperAdminType, "Система", "", "", "TNskDataserver", "pi31415926","9130000000");
    public final static String NskGorTransURL="maps.nskgortrans.ru";
    public final static int FirstStatisticHour=6;
    public final static int LastStatisticHour=24;
    //-----------------------------------------------------------------------------
    public final static int PopupMessageDelay=6;                // Тайм-аут всплывающего окна
    public final static int PopupLongDelay=20;                  // Тайм-аут всплывающего окна
    private  final static String EMClassNames[]={
            "romanow.abc.core.constants.Values",
            "romanow.abc.core.entity.WorkSettings",
            "romanow.abc.dataserver.TNskDataServer",
            "romanow.abc.dataserver.TNskConsoleServer",
            "romanow.abc.desktop.Cabinet",
            "romanow.abc.desktop.TNSKClient",
            "","",""};
    private  final static String EMAppNames[]={
            "tnsk",
            "tnsk",
            "tnsk",
            "tnsk",
            "TNsk.apk",
            "TNskDataserver.jar",
            "romanow.abc.desktop.module",
            "/drawable/busstop.png",
            "Транспорт-NSK"
            };
    @Override
    protected void initEnv() {
        super.initEnv();
        I_Environment env = new I_Environment() {
            @Override
            public String applicationClassName(int classType) {
                return EMClassNames[classType];
            }

            @Override
            public String applicationName(int nameNype) {
                return EMAppNames[nameNype];
            }

            @Override
            public User superUser() {
                return superUser;
            }

            @Override
            public Class applicationClass(int classType) throws UniException {
                return createApplicationClass(classType, EMClassNames);
            }

            @Override
            public Object applicationObject(int classType) throws UniException {
                return createApplicationObject(classType, EMClassNames);
            }

            @Override
            public int releaseNumber() {
                return EMReleaseNumber;
            }

            @Override
            public WorkSettingsBase currentWorkSettings() {
                return new WorkSettings();
            }
        };
        setEnv(env);
        }
    @Override
    protected void initTables(){
        super.initTables();
        EntityIndexedFactory EntityFactory = getEntityFactory();
        EntityFactory.put(new TableItem("Точка сегмента", TSegPoint.class).add("TSegment"));
        EntityFactory.put(new TableItem("Сегмент", TSegment.class));
        EntityFactory.put(new TableItem("Остановка", TStop.class));
        EntityFactory.put(new TableItem("Маршрут", TRoute.class));
        EntityFactory.put(new TableItem("Сегмент маршрута", TRouteSegment.class));
        EntityFactory.put(new TableItem("Остановка маршрута", TRouteStop.class));
        EntityFactory.put(new TableItem("Форматы SMS", TSMSBank.class));
        EntityFactory.put(new TableItem("Настройки", WorkSettings.class));
        HashMap<String,String> PrefixMap = getPrefixMap();
        PrefixMap.put("TSegPoint.gps","g");
        PrefixMap.put("TStop.gps","g");
        /*
        EntityFactory.put(new TableItem("Мета:Внешняя подсистема", MetaExternalSystem.class));
        EntityFactory.put(new TableItem("Мета:Подсистемы", MetaSubSystem.class));
        EntityFactory.put(new TableItem("Мета:Состояние", MetaState.class).add("MetaExternalSystem"));
         */
       }
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Тьютор")
    public final static int UserTutor = 3;
    @CONST(group = "User", title = "Преподаватель")
    public final static int UserTeacher = 4;
    @CONST(group = "User", title = "Студент")
    public final static int UserStudent = 5;
    //----------------------- Отчеты  ---------------------------------------------
    @CONST(group = "Report", title = "Рейтинг группы")
    public final static int RepGroup = 1;
    @CONST(group = "Report", title = "Рейтинг студента")
    public final static int RepStudent = 2;
    @CONST(group = "Report", title = "Рейтинг уч.единицы")
    public final static int RepEduUnit = 3;
    //----------------------- Типы ТС  ---------------------------------------------
    @CONST(group = "RouteType", title = "Авт.")
    public final static int RouteBus = 0;
    @CONST(group = "RouteType", title = "Тролл.")
    public final static int RouteTrolleyBus = 1;
    @CONST(group = "RouteType", title = "Трам.")
    public final static int RouteTram = 2;
    @CONST(group = "RouteType", title = "Марш.")
    public final static int RouteMinibus = 7;
    //----------------------- Вариант перекрытия сегментов -------------------------
    @CONST(group = "OverlapMode", title = "Прямое полное")
    public final static int OMRightFull = 0;
    @CONST(group = "OverlapMode", title = "Обратное полное")
    public final static int OMInvertFull = 1;
    @CONST(group = "OverlapMode", title = "Прямое частично от начала")
    public final static int OMRightPartBeg = 2;
    @CONST(group = "OverlapMode", title = "Прямое частично от конца")
    public final static int OMRightPartEnd = 3;
    @CONST(group = "OverlapMode", title = "Обратное частично от начала")
    public final static int OMInvertPartBeg = 4;
    @CONST(group = "OverlapMode", title = "Обратное частично от конца")
    public final static int OMInvertPartEnd = 5;
    //----------------------- Состояние точки пассажира -------------------------
    @CONST(group = "PPState", title = "Не определен")
    public final static int PPStateNone = 0;
    @CONST(group = "PPState", title = "Не привязан к борту")
    public final static int PPStateOffBoard = 1;
    @CONST(group = "PPState", title = "Превышены критерии")
    public final static int PPStateOver = 2;
    @CONST(group = "PPState", title = "Привязан к борту")
    public final static int PPStateOnBoard = 3;
    @CONST(group = "PPState", title = "Ожидание доп.данных")
    public final static int PPStateContinue = 4;
    @CONST(group = "PPState", title = "Не движется")
    public final static int PPStateNotMoving = 5;
    @CONST(group = "PPState", title = "Промежуточная+")
    public final static int PPStateSecondOn = 6;
    @CONST(group = "PPState", title = "Промежуточная-")
    public final static int PPStateSecondOff = 7;
    //-------------------------------------------------------------------------------------------
    public static void main(String a[]){
        Values.init();
        System.out.println(title("User", UserAdminType));
        System.out.print(Values.constMap().toString());
        }
}
