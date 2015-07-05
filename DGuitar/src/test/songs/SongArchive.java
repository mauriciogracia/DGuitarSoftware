/*
 * Created on Mar 19, 2005
 */
package test.songs;


/**
 * @author Chris
 */
public class SongArchive
{

    public static MidiSongDefinition aguaSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/Soda Stereo - Hombre Al Agua.gp3");
        sd.setMidiFileName("src/test/Soda Stereo - Hombre Al Agua.mid");
        sd.addComponent(new RepeatedSongComponent(4, 4, 1, 4, 1));
        sd.addComponent(new RepeatedSongComponent(4, 4, 5, 8, 4));
        sd.addComponent(new SongComponent(4, 4, 9, 40));
        sd.addComponent(new RepeatedSongComponent(4, 4, 41, 42, 1));
        sd.addComponent(new SongComponent(4, 4, 43, 43));
        sd.setChannels(new int[] { 5, 6, 3, 4, 1, 2, 7, 8, 9, 11 });

        // Agua's tie notes are now handled by the player
        
        return sd;
    }

    public static MidiSongDefinition furiaSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/Soda Stereo - Ciudad De La Furia.gp3");
        sd.setMidiFileName("src/test/Soda Stereo - Ciudad De La Furia.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 2));
        sd.addComponent(new RepeatedSongComponent(4, 4, 3, 5, 1));
        sd.addComponent(new SongComponent(4, 4, 6, 8));
        sd.addComponent(new RepeatedSongComponent(4, 4, 9, 10, 1));
        sd.addComponent(new SongComponent(4, 4, 11, 14));
        sd.addComponent(new RepeatedSongComponent(4, 4, 15, 16, 1));
        sd.addComponent(new RepeatedSongComponent(4, 4, 17, 18, 1));
        sd.addComponent(new RepeatedSongComponent(4, 4, 19, 20, 1));
        sd.addComponent(new RepeatedSongComponent(4, 4, 21, 22, 1));
        sd.setChannels(new int[] { 1, 2, 3, 4 });
        return sd;
    }

    public static MidiSongDefinition opethSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/Opeth - Deliverance.gp4");
        sd.setMidiFileName("src/test/Opeth - Deliverance.mid");
        sd.addComponent(new SongComponent(7, 8, 1, 20));
        sd.addComponent(new SongComponent(4, 4, 21, 55));
        sd.addComponent(new SongComponent(6, 8, 56, 56));
        sd.addComponent(new SongComponent(4, 4, 57, 59));
        sd.addComponent(new SongComponent(7, 8, 60, 196));
        sd.addComponent(new SongComponent(6, 8, 197, 197));
        sd.addComponent(new SongComponent(7, 8, 198, 198));
        sd.addComponent(new SongComponent(9, 8, 199, 199));
        sd.addComponent(new SongComponent(7, 8, 200, 200));
        sd.addComponent(new SongComponent(9, 8, 201, 201));
        sd.addComponent(new SongComponent(7, 8, 202, 674));
        sd.setChannels(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 10, 10 });
        return sd;
    }

    public static MidiSongDefinition empty2SongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/empty-2track.gp4");
        sd.setMidiFileName("src/test/empty-2track.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 1));
        sd.setChannels(new int[] { 1, 2, 3, 4 });
        return sd;
    }

    public static MidiSongDefinition empty1SongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/empty-1track.gp4");
        sd.setMidiFileName("src/test/empty-1track.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 1));
        sd.setChannels(new int[] { 1, 2 });
        return sd;
    }

    public static MidiSongDefinition effectsSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/effects.gp4");
        sd.setMidiFileName("src/test/effects.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 3));
        sd.setChannels(new int[] { 1, 2 });
        return sd;
    }

    public static MidiSongDefinition displaySongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/display.gp4");
        sd.setMidiFileName("src/test/display.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 6));
        sd.setChannels(new int[] { 1, 2, 3, 4 });
        return sd;
    }

    public static MidiSongDefinition blueSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/blue.gp4");
        sd.setMidiFileName("src/test/blue.mid");
        sd.addComponent(new RepeatedSongComponent(3, 4, 1, 3, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 4, 6, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 7, 13, 1));
        sd.addComponent(new SongComponent(3, 4, 14, 15));
        sd.setChannels(new int[] { 1, 2, 3, 4 });
        return sd;
    }

    public static MidiSongDefinition bendsSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/bends.gp4");
        sd.setMidiFileName("src/test/bends.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 2));
        sd.setChannels(new int[] { 1, 2 });
        return sd;
    }

    public static MidiSongDefinition fourhandsSongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/4 hands.gp4");
        sd.setMidiFileName("src/test/4 hands.mid");
        sd.addComponent(new RepeatedSongComponent(3, 4, 1, 4, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 5, 8, 1));
        sd.addComponent(new SongComponent(3, 4, 9, 14));
        sd.addComponent(new RepeatedSongComponent(3, 4, 15, 18, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 19, 22, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 23, 24, 1));
        sd.addComponent(new RepeatedSongComponent(3, 4, 25, 27, 1));
        sd.addComponent(new SongComponent(3, 4, 28, 39));
        sd.setChannels(new int[] { 1, 2, 3, 4 });
        
        // events included in the remap: instrument and volume change
        sd.setEventRemap(new String[]
                                    {
                						"0:C06300",		"0:C01800",
                						"0:B00730",		"0:B00740",
                						"0:C26600",		"0:C24900",
                						"0:B20760",		"0:B20750",
                						"5040:903400",	"30240:903400",
                						"20160:904700",	"30240:904700",                						
                						"35280:903200",	"60480:903200",
                						"50400:904500",	"60480:904500",                						
                						"65520:903000",	"90720:903000",
                						"80640:904300",	"100800:904300",
                						"126000:903400",	"151200:903400",
                						"141120:904700",	"151200:904700",                						
                						"156240:903200",	"181440:903200",
                						"171360:904500",	"181440:904500",                						
                						"186480:903000",	"211680:903000",
                						"201600:904300",	"221760:904300",
                						"246960:903400",	"252000:903400",
                						"262080:904200",	"272160:904200",
                						"262080:924700",	"267120:924700",                						
                						"277200:903200",	"282240:903200",
                						"292320:904000",	"302400:904000",
                						"292320:924500",	"297360:924500",                						
                						"307440:903000",	"312480:903000",
                						"322560:903E00",	"342720:903E00",
                						"322560:924300",	"342720:924300",             						               						
                						"362880:924500",	"383040:924500",                						
                						"367920:903400",	"372960:903400",
                						"383040:904200",	"393120:904200",
                						"383040:924700",	"388080:924700",                						
                						"398160:903200",	"403200:903200",
                						"413280:904000",	"423360:904000",
                						"413280:924500",	"418320:924500",                						
                						"428400:903000",	"433440:903000",
                						"443520:903E00",	"463680:903E00",
                						"443520:924300",	"463680:924300",                						
                						"483840:924500",	"504000:924500",               						
                						"498960:923400",	"509040:923400",
                						"504000:904700",	"514080:904700",
                						"504000:924200",	"514080:924200",
                						"529200:923200",	"539280:923200",
                						"534240:904500",	"544320:904500",
                						"534240:924000",	"544320:924000",
                						"549360:923000",	"554400:923000",
                						"559440:923000",	"569520:923000",
                						"564480:904300",	"584640:904300",
                						"564480:923E00",	"584640:923E00",
                						"574560:923000",	"584640:923000",
                						"670320:902B00",	"695520:902B00",
                						"685440:904300",	"710640:904300",
                						"700560:902F00",	"725760:902F00",
                						"715680:904300",	"740880:904300",
                						"730800:903000",	"756000:903000",
                						"745920:904300",	"771120:904300",
                						"761040:903400",	"786240:903400",
                						"776160:904300",	"801360:904300",
                						"791280:902B00",	"816480:902B00",
                						"806400:904300",	"831600:904300",
                						"821520:902F00",	"846720:902F00",
                						"836640:904300",	"861840:904300",
                						"851760:903000",	"876960:903000",
                						"866880:904300",	"892080:904300",
                						"882000:903400",	"907200:903400",
                						"897120:904300",	"922320:904300",
                						"912240:902B00",	"937440:902B00",
                						"927360:904300",	"952560:904300",
                						"942480:902F00",	"967680:902F00",
                						"957600:904300",	"982800:904300",
                						"972720:903000",	"997920:903000",
                						"987840:904300",	"1013040:904300",
                						"1002960:903400",	"1028160:903400",
                						"1018080:904300",	"1028160:904300",
                						"1028160:924000",	"1028160:924000",
                						"1033200:902B00",	"1058400:902B00",
                						"1048320:904300",	"1073520:904300",
                						"1063440:902F00",	"1088640:902F00",
                						"1078560:904300",	"1103760:904300",
                						"1093680:903000",	"1118880:903000",
                						"1108800:904300",	"1134000:904300",
                						"1123920:903400",	"1149120:903400",
                						"1139040:904300",	"1149120:904300",
                						"1154160:904300",	"1164240:904300",
                						"1164240:903900",	"1174320:903900",
                						"1169280:904300",	"1179360:904300",
                						"1179360:903900",	"1189440:903900",
                						"1184400:904500",	"1194480:904500",
                						"1194480:903900",	"1204560:903900",
                						"1199520:904500",	"1209600:904500",
                						"1214640:904300",	"1224720:904300",
                						"1224720:903900",	"1234800:903900",
                						"1229760:904300",	"1239840:904300",
                						"1239840:903900",	"1249920:903900",
                						"1244880:904500",	"1254960:904500",
                						"1254960:903900",	"1265040:903900",
                						"1260000:904500",	"1270080:904500",
                  						"1270080:90434F",	"1270332:90434F",
                						"1270080:903E4F",	"1270290:903E4F",
                  						"1270080:90374F",	"1270248:90374F",
                   						"1270080:90324F",	"1270185:90324F",
                						"1270080:902F4F",	"1270122:902F4F",
                						"1280160:903E3F",	"1280202:903E3F",
                						"1280160:90373F",	"1280265:90373F",
                						"1280160:90323F",	"1280328:90323F",
                   						"1280160:902F3F",	"1280370:902F3F",
                   						"1280160:902B3F",	"1280412:902B3F",
                   						"1285200:90433F",	"1285452:90433F",
                   						"1285200:903E3F",	"1285410:903E3F",
                   						"1285200:90373F",	"1285368:90373F",
                   						"1285200:90323F",	"1285305:90323F",
                   						"1285200:902F3F",	"1285242:902F3F",
                   						"1290240:90434F",	"1291290:90434F",
                   						"1290240:903E4F",	"1291080:903E4F",
                   						"1290240:90374F",	"1290870:90374F",
                   						"1290240:90324F",	"1290660:90324F",
                   						"1290240:902F4F",	"1290450:902F4F",
                   						"1300320:90434F",	"1301370:90434F",
                   						"1300320:903E4F",	"1301160:903E4F",
                   						"1300320:90374F",	"1300950:90374F",
                   						"1300320:90324F",	"1300740:90324F",
                   						"1300320:902F4F",	"1300530:902F4F",
                   						"1310400:90434F",	"1311240:90434F",
                   						"1310400:903E4F",	"1311030:903E4F",
                   						"1310400:90374F",	"1310820:90374F",
                   						"1310400:90344F",	"1310610:90344F",
                   						"1330560:90434F",	"1331400:90434F",
                   						"1330560:903E4F",	"1331190:903E4F",
                   						"1330560:90374F",	"1330980:90374F",
                   						"1330560:90344F",	"1330770:90344F",
                   						"1340640:90434F",	"1341480:90434F",
                   						"1340640:903E4F",	"1341270:903E4F",
                   						"1340640:90374F",	"1341060:90374F",
                   						"1340640:90344F",	"1340850:90344F",
                   						"1360800:90434F",	"1361052:90434F",
                   						"1360800:903E4F",	"1361010:903E4F",
                   						"1360800:90374F",	"1360968:90374F",
                   						"1360800:90324F",	"1360905:90324F",
                   						"1360800:902F4F",	"1360842:902F4F",
                						"1370880:903E3F",	"1370922:903E3F",
                						"1370880:90373F",	"1370985:90373F",
                						"1370880:90323F",	"1371048:90323F",
                   						"1370880:902F3F",	"1371090:902F3F",
                   						"1370880:902B3F",	"1371132:902B3F",
                   						"1375920:90433F",	"1376172:90433F",
                   						"1375920:903E3F",	"1376130:903E3F",
                   						"1375920:90373F",	"1376088:90373F",
                   						"1375920:90323F",	"1376025:90323F",
                   						"1375920:902F3F",	"1375962:902F3F",
                   						"1380960:90434F",	"1382010:90434F",
                   						"1380960:903E4F",	"1381800:903E4F",
                   						"1380960:90374F",	"1381590:90374F",
                   						"1380960:90324F",	"1381380:90324F",
                   						"1380960:902F4F",	"1381170:902F4F",
                   						"1391040:90434F",	"1392090:90434F",
                   						"1391040:903E4F",	"1391880:903E4F",
                   						"1391040:90374F",	"1391670:90374F",
                   						"1391040:90324F",	"1391460:90324F",
                   						"1391040:902F4F",	"1391250:902F4F",
                   						"1401120:90434F",	"1401960:90434F",
                   						"1401120:903E4F",	"1401750:903E4F",
                   						"1401120:90374F",	"1401540:90374F",
                   						"1401120:90344F",	"1401330:90344F",
                   						"1421280:90434F",	"1422120:90434F",
                   						"1421280:903E4F",	"1421910:903E4F",
                   						"1421280:90374F",	"1421700:90374F",
                   						"1421280:90344F",	"1421490:90344F",
                   						"1431360:90434F",	"1432200:90434F",
                   						"1431360:903E4F",	"1431990:903E4F",
                   						"1431360:90374F",	"1431780:90374F",
                   						"1431360:90344F",	"1431570:90344F",
                   						"1572480:924500",	"1582560:924500",
                   						"1711080:903200",	"1753920:903200",
                   						"1711080:923200",	"1753920:923200",
                   						"1713600:903900",	"1753920:903900",
                   						"1713600:923900",	"1753920:923900",
                   						"1716120:903E00",	"1753920:903E00",
                   						"1716120:923E00",	"1753920:923E00",
                   						"1784160:904100",	"1814400:904100",
                  			
                   						}
                                    );
        
        return sd;
    }

    public static MidiSongDefinition xraySongDefinition()
    {
        MidiSongDefinition sd = new MidiSongDefinition();
        sd.setGpFileName("src/test/X-ray Song.gp4");
        sd.setMidiFileName("src/test/X-ray Song.mid");
        sd.addComponent(new SongComponent(4, 4, 1, 116));
        sd.setChannels(new int[] { 1, 1, 2, 2, 3, 3, 4, 5, 6, 7, 8, 8, 9, 9,
                10, 10 });
        return sd;
    }

    public static SongDefinition somethingSongDefinition()
    {
        SongDefinition sd = new SongDefinition();
        sd.setGpFileName("src/test/Beatles (The) - Something (2).gp3");
        sd.addComponent(new SongComponent(1, 4, 1, 1));
        sd.addComponent(new SongComponent(4, 4, 2, 50));
        sd.setChannels(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 10, 10 });
        return sd;
    }

    public static SongDefinition corneilleSongDefinition()
    {
        SongDefinition sd = new SongDefinition();
        sd.setGpFileName("src/test/Corneille - Parce Qu'on Viens De Loin.gp4");
        return sd;
    }

    /**
     * @return a MidiSongDefinition with the test files
     */
    public static MidiSongDefinition testFileSongDefinition()
    {
        MidiSongDefinition sd=new MidiSongDefinition();
        sd.setGpFileName("src/test/test.gp4");
        sd.setMidiFileName("src/test/test.mid");
        return sd;
    }

}