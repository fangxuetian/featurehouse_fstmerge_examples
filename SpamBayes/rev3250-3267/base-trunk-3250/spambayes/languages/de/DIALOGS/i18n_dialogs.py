_rc_size_=30899
_rc_mtime_=1173774820
try:
    _
except NameError:
    def _(s):
        return s
class FakeParser:
    dialogs = {'IDD_WIZARD_FINISHED_TRAIN_LATER': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Konfiguration angehalten'), -1, (20, 4, 247, 14), 1342177280], [130, _("Um mit dem Training zu beginnen, sollten Sie einen Ordner erstellen, der nur Beispiele von 'guten' Nachrichten enth\xe4lt und einen, der nur Beispiele von Spam enth\xe4lt."), -1, (20, 17, 257, 27), 1342177280], [130, _("Klicken Sie auf 'Beenden', um den Assistenten zu schlie\xdfen."), -1, (20, 145, 228, 9), 1342177280], [130, _("F\xfcr Beispiele von 'guten' Nachrichten k\xf6nnen Sie z.B. den Posteingang benutzen. Es ist aber wichtig, aus diesem erst den gesamten Spam zu entfernen, bevor Sie fortfahren. Wenn Sie zuviel in Ihrem Posteingang haben, k\xf6nnen Sie auch einen Teil davon in einen tempor\xe4ren Ordner kopieren."), -1, (20, 44, 256, 36), 1342177280], [130, _("F\xfcr Beispiele von Spam k\xf6nnen Sie z.B. im Posteingang oder in 'Gel\xf6schte Objekte' suchen. SpamBayes erlaubt jedoch nicht, den Ordner 'Gel\xf6schte Objekte' selbst anzugeben. Sie k\xf6nnen jedoch die Elemente aus 'Gel\xf6schte Objekte' in einen selbst angelegten Ordner kopieren."), -1, (20, 80, 247, 35), 1342177280], [130, _('Wenn Sie fertig damit sind, starten Sie den SpamBayes Installationsassistenten erneut und konfigurieren Sie SpamBayes.'), -1, (20, 121, 257, 17), 1342177280]], 'IDD_WIZARD_WELCOME': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Willkommen zum SpamBayes Konfigurationsassistenten'), -1, (20, 4, 191, 14), 1342177280], [130, _('Dieser Assistent hilft Ihnen, SpamBayes einzurichten. Bitte geben Sie an, wie Sie sich auf den Umgang mit dem Programm vorbereitet haben.'), -1, (20, 20, 255, 18), 1342177280], [128, _('Ich habe gar nichts vorbereitet.'), 1081, (20, 42, 190, 11), 1342309385], [128, _("Ich habe bereits Spam und 'gute' Nachrichten (Ham) in f\xfcr das Training geeignete Ordner sortiert."), -1, (20, 59, 255, 18), 1342186505], [128, _('Ich bevorzuge, SpamBayes manuell zu konfigurieren (Expertenmodus)'), -1, (20, 82, 255, 12), 1342178313], [130, _("Wenn Sie mehr \xfcber die Konfiguration und das Training von SpamBayes erfahren m\xf6chten, dr\xfccken Sie den Knopf '\xdcber...'"), -1, (20, 103, 206, 22), 1342177280], [128, _('\xdcber...'), 1017, (233, 104, 42, 15), 1342177280], [130, _('Wenn Sie den SpamBayes Konfigurationsassistenten abbrechen, k\xf6nnen Sie ihn jederzeit \xfcber den SpamBayes Manager von der Outlook Symbolleiste neu starten.'), -1, (20, 129, 247, 26), 1342177280]], 'IDD_WIZARD': [[_('SpamBayes Konfigurationsassistent'), (0, 0, 384, 190), -1865940800, 1024, (8, 'Tahoma')], [128, _('Abbrechen'), 2, (328, 173, 50, 14), 1342177280], [128, _('<< Zur\xfcck'), 1069, (216, 173, 50, 14), 1342177280], [128, _('Weiter >>,Beenden'), 1077, (269, 173, 50, 14), 1342177281], [130, '', 1078, (75, 4, 303, 167), 1342177298], [130, '125', 1092, (0, 0, 69, 190), 1342177294]], 'IDD_WIZARD_FINISHED_UNTRAINED': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Gratulation'), -1, (20, 4, 247, 14), 1342177280], [130, _('SpamBayes ist jetzt konfiguriert und bereit, \xfcber ihre Nachrichten zu lernen.'), -1, (20, 22, 247, 16), 1342177280], [130, _("Weil SpamBayes nicht trainiert ist, landen alle Nachrichten im Ordner 'unsicher'. Bitte benutzen Sie die Schaltfl\xe4chen  'Spam' und 'Kein Spam', um SpamBayes zu trainieren."), -1, (20, 42, 247, 27), 1342177280], [130, _('Wenn Sie die Lernzeit verk\xfcrzen wollen, verschieben Sie allen vorhandenen Spam in einen Ordner und trainieren danach SpamBayes mit Hilfe des SpamBayes Managers.'), -1, (20, 94, 247, 31), 1342177280], [130, _('Wenn Sie SpamBayes auf diese Weise trainieren, werden Sie feststellen, dass die Genauigkeit von SpamBayes zunimmt.'), -1, (20, 69, 247, 18), 1342177280], [130, _("Klicken Sie 'Beenden', um den Assistenten zu schlie\xdfen."), -1, (20, 132, 263, 9), 1342177280]], 'IDD_GENERAL': [[_('Allgemein'), (0, 0, 253, 257), 1354760256, 1024, (8, 'Tahoma')], [130, _('SpamBayes Version Here'), 1009, (6, 54, 242, 8), 1342177280], [130, _('SpamBayes ben\xf6tigt Training, bevor es effektiv arbeiten kann. Klicken Sie auf die Registerkarte Training, um das Training durchzuf\xfchren.'), -1, (6, 67, 242, 17), 1342177280], [130, _('Status der Training Datenbank'), -1, (6, 90, 222, 8), 1342177280], [130, _('123 spam messages; 456 good messages\\r\\nLine2\\r\\nLine3'), 1035, (6, 101, 242, 27), 1342181376], [128, _('SpamBayes aktivieren'), 1013, (6, 221, 97, 11), 1342242819], [130, _('Certain spam is moved to Folder1\\nPossible spam is moved too'), 1014, (6, 146, 242, 67), 1342181376], [128, _('Konfiguration zur\xfccksetzen...'), 1073, (6, 238, 108, 15), 1342177280], [128, _('Konfigurationsassistent...'), 1070, (155, 238, 93, 15), 1342177280], [130, _('Filter Status:'), -1, (6, 135, 222, 8), 1342177280], [130, '1062', 1063, (0, 2, 275, 52), 1342179342]], 'IDD_WIZARD_FINISHED_UNCONFIGURED': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Konfiguration abgebrochen'), -1, (20, 4, 247, 14), 1342177280], [130, _('Die SpamBayes Optionen werden jetzt angezeigt. Sie m\xfcssen Ihre Ordner ausw\xe4hlen, bevor SpamBayes beginnt, Nachrichten zu filtern.'), -1, (20, 29, 247, 16), 1342177280], [130, _("Klicken Sie auf 'Beenden', um den Assistenten zu beenden."), -1, (20, 139, 240, 16), 1342177280]], 'IDD_MANAGER': [[_('SpamBayes Manager'), (0, 0, 275, 308), -1865940800, 1024, (8, 'Tahoma')], [128, _('Schlie\xdfen'), 1, (216, 287, 50, 14), 1342177281], [128, _('Abbrechen'), 2, (155, 287, 50, 14), 1073741824], ['SysTabControl32', '', 1068, (8, 7, 258, 276), 1342177280], [128, _('\xdcber...'), 1072, (8, 287, 50, 14), 1342177280]], 'IDD_WIZARD_FOLDERS_REST': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [128, _('Durchsuchen'), 1005, (208, 100, 60, 15), 1342177280], [130, _("Ordner f\xfcr Spam und 'unsichere' Nachrichten"), -1, (20, 4, 247, 14), 1342177280], [130, _('SpamBayes benutzt zwei Ordner, um Spam zu verwalten. Einen Ordner, der Nachrichten enth\xe4lt, bei denen sich SpamBayes sicher ist und einen, wo es unsicher ist.'), -1, (20, 20, 247, 29), 1342177280], [130, _("Wenn Sie einen Ordnernamen eingeben, der nicht existiert, wird ein Ordner mit diesem Namen erstellt. Sollten Sie einen bereits existierenden Ordner bevorzugen, klicken Sie auf 'Durchsuchen', um den Ordner auszuw\xe4hlen."), -1, (20, 53, 243, 24), 1342177280], [129, '', 1027, (20, 100, 179, 14), 1350566016], [130, _('Unsichere Nachrichten kommen in folgenden Ordner:'), -1, (20, 121, 227, 12), 1342177280], [129, '', 1033, (20, 132, 177, 14), 1350566016], [130, _('Spam soll in folgenden Ordner zugestellt werden:'), -1, (20, 89, 189, 8), 1342177280], [128, _('Durchsuchen'), 1034, (208, 132, 60, 15), 1342177280]], 'IDD_WIZARD_TRAIN': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Training'), -1, (20, 4, 247, 14), 1342177280], [130, _('SpamBayes wird trainiert anhand Ihrer guten Nachrichten und Ihres Spams'), -1, (20, 22, 247, 16), 1342177280], ['msctls_progress32', '', 1000, (20, 45, 255, 11), 1350565888], [130, _('(progress text)'), 1001, (20, 61, 257, 10), 1342177280]], 'IDD_DIAGNOSTIC': [[_('Diagnose'), (0, 0, 183, 98), -1865940800, 1024, (8, 'Tahoma')], [130, _('Diese erweiterten Optionen sind nur f\xfcr die Fehlersuche gedacht. Sie sollten hier nur Werte \xe4ndern, wenn Sie dazu aufgefordert wurden oder wenn Sie genau wissen, was sie bedeuten.'), -1, (5, 3, 174, 36), 1342177280], [130, _('Ausf\xfchrlichkeit Logdatei'), -1, (5, 44, 77, 8), 1342177280], [129, '', 1061, (84, 42, 31, 14), 1350566016], [128, _('Log ansehen...'), 1093, (117, 41, 62, 14), 1342177280], [128, _('Spamwert sichern'), 1048, (5, 63, 72, 10), 1342242819], [128, _('Abbrechen'), 2, (69, 79, 50, 14), 1073741824], [128, _('Schlie\xdfen'), 1, (129, 79, 50, 14), 1342177281]], 'IDD_FILTER': [[_('Filtern'), (0, 0, 249, 257), 1354760256, 1024, (8, 'Tahoma')], [130, _('Die folgenden Ordner filtern beim Eintreffen neuer Nachrichten'), -1, (8, 4, 207, 11), 1342177280], [130, _('Folder names...\\nLine 2'), 1038, (7, 16, 177, 12), 1342312972], [128, _('Durchsuchen'), 1039, (192, 14, 50, 14), 1342177280], [128, _('Zweifelsfrei Spam'), -1, (7, 31, 235, 82), 1342177287], [130, _('Um sicher Spam zu sein, muss der Spamwert mindestens betragen:'), -1, (12, 40, 225, 10), 1342177280], ['msctls_trackbar32', _('Slider1'), 1023, (13, 50, 165, 22), 1342242821], [129, '', 1024, (184, 53, 51, 14), 1350566016], [130, _('und folgende Aktion soll mit dieser Nachricht durchgef\xfchrt werden:'), -1, (13, 72, 223, 10), 1342177280], [133, '', 1025, (12, 83, 55, 40), 1344339971], [130, _('in Ordner'), -1, (71, 85, 31, 10), 1342177280], [130, _('Ordner Namen'), 1027, (103, 83, 77, 14), 1342312972], [128, _('Durchsuchen'), 1028, (184, 83, 50, 14), 1342177280], [128, _('M\xf6glicherweise Spam'), -1, (6, 117, 235, 84), 1342177287], [130, _('Um als unsicher gelten, muss der Spamwert mindestens betragen:'), -1, (12, 128, 212, 10), 1342177280], ['msctls_trackbar32', _('Slider1'), 1029, (12, 137, 165, 20), 1342242821], [129, '', 1030, (183, 141, 54, 14), 1350566016], [130, _('und folgende Aktion soll mit dieser Nachricht durchgef\xfchrt werden:'), -1, (12, 158, 217, 10), 1342177280], [133, '', 1031, (12, 169, 55, 40), 1344339971], [130, _('in Ordner'), -1, (71, 172, 31, 10), 1342177280], [130, _('(folder name)'), 1033, (103, 169, 77, 14), 1342312972], [128, _('Durchsuchen'), 1034, (184, 169, 50, 14), 1342177280], [128, _('Spam als gelesen markieren'), 1047, (13, 100, 154, 10), 1342242819], [128, _('M\xf6glichen Spam als gelesen markieren'), 1051, (12, 189, 190, 10), 1342242819], [128, _('Sicher gut'), -1, (6, 206, 235, 48), 1342177287], [130, _('Aktion f\xfcr gute Nachrichten:'), -1, (12, 218, 107, 10), 1342177280], [133, '', 1032, (12, 231, 55, 40), 1344339971], [130, _('in Ordner'), -1, (71, 233, 31, 10), 1342177280], [130, _('(folder name)'), 1083, (103, 231, 77, 14), 1342312972], [128, _('Durchsuchen'), 1004, (184, 231, 50, 14), 1342177280]], 'IDD_FILTER_NOW': [[_('Jetzt filtern'), (0, 0, 244, 185), -1865940800, 1024, (8, 'Tahoma')], [130, _('Die folgenden Ordner filtern'), -1, (8, 9, 168, 11), 1342177280], [130, _('Folder names...\\nLine 2'), 1036, (7, 20, 172, 12), 1342181900], [128, _('Durchsuchen'), 1037, (187, 19, 50, 14), 1342177280], [128, _('Filteraktionen'), -1, (7, 38, 230, 40), 1342308359], [128, _('Alle Aktionen ausf\xfchren'), 1019, (15, 49, 126, 10), 1342373897], [128, _('Nachrichten bewerten, aber keine Aktionen ausf\xfchren'), 1018, (15, 62, 203, 10), 1342177289], [128, _('Filter beschr\xe4nken'), -1, (7, 84, 230, 35), 1342308359], [128, _('Nur ungelesene Nachrichten bearbeiten'), 1020, (15, 94, 149, 9), 1342242819], [128, _('Nur ungefilterte Nachrichten verarbeiten'), 1021, (15, 106, 149, 9), 1342242819], ['msctls_progress32', _('Progress1'), 1000, (7, 129, 230, 11), 1350565888], [130, _('Static'), 1001, (7, 144, 227, 10), 1342177280], [128, _('Start filtern'), 1006, (7, 161, 52, 14), 1342177281], [128, _('Schlie\xdfen'), 2, (187, 162, 50, 14), 1342177280]], 'IDD_TRAINING': [[_('Training'), (0, 0, 252, 257), 1354760256, 1024, (8, 'Tahoma')], [128, '', -1, (5, 1, 243, 113), 1342177287], [130, _('Ordner mit bekannterma\xdfen guten Nachrichten'), -1, (11, 11, 170, 11), 1342308364], [130, '', 1002, (11, 21, 175, 12), 1342181900], [128, _('Durchsuchen'), 1004, (192, 20, 50, 14), 1342177280], [130, _('Ordner mit Spam oder anderen M\xfcllnachrichten'), -1, (11, 36, 171, 9), 1342177280], [130, _('Static'), 1003, (11, 46, 174, 12), 1342312972], [128, _('Durchsuchen'), 1005, (192, 46, 50, 14), 1342177280], [128, _('Nachrichten nach Training bewerten'), 1008, (11, 64, 131, 10), 1342242819], [128, _('Datenbank komplett neu'), 1007, (147, 64, 94, 10), 1342242819], ['msctls_progress32', _('Progress1'), 1000, (11, 76, 231, 11), 1350565888], [128, _('Training &starten'), 1006, (11, 91, 54, 14), 1342193664], [130, _('training status training status training status training status training status training status training status '), 1001, (75, 89, 149, 17), 1342177280], [128, _('InkrementellesTraining'), -1, (4, 117, 244, 87), 1342177287], [128, _("Trainieren, dass eine Nachricht 'gut' ist, wenn sie aus einem Spam-Ordner in den Posteingang verschoben wird"), 1010, (11, 127, 204, 18), 1342251011], [130, _("Klicken auf 'Kein Spam' soll die Nachricht..."), -1, (10, 148, 141, 10), 1342177280], [133, '', 1075, (153, 145, 88, 54), 1344339971], [128, _('Trainieren, dass eine Nachricht Spam ist, wenn sie in den Spam-Ordner verschoben wird.'), 1011, (11, 163, 204, 16), 1342251011], [130, _("Klicken auf 'Spam' soll die Nachricht..."), -1, (10, 183, 140, 10), 1342177280], [133, '', 1074, (153, 180, 88, 54), 1344339971]], 'IDD_NOTIFICATIONS': [[_('Notifizierung'), (0, 0, 248, 257), 1354760256, None, (8, 'Tahoma')], [128, _('Kl\xe4nge f\xfcr neue Nachrichten'), -1, (7, 3, 241, 229), 1342177287], [128, _('Kl\xe4nge f\xfcr neue Nachrichten aktivieren'), 1098, (14, 17, 140, 10), 1342242819], [130, _('Gute Nachricht:'), -1, (14, 31, 51, 8), 1342177280], [129, '', 1094, (14, 40, 174, 14), 1350566016], [128, _('Durchsuchen'), 1101, (192, 40, 50, 14), 1342177280], [130, _('Unsichere Nachricht:'), -1, (14, 58, 67, 8), 1342177280], [129, '', 1095, (14, 67, 174, 14), 1350566016], [128, _('Durchsuchen'), 1102, (192, 67, 50, 14), 1342177280], [130, _('Spam:'), -1, (14, 85, 21, 8), 1342177280], [129, '', 1096, (14, 94, 174, 14), 1350566016], [128, _('Durchsuchen'), 1103, (192, 94, 50, 14), 1342177280], [130, _('Zeit, um auf weitere Nachrichten zu warten'), -1, (14, 116, 142, 8), 1342177280], ['msctls_trackbar32', '', 1099, (14, 127, 148, 22), 1342242821], [129, '', 1100, (163, 133, 40, 14), 1350566016], [130, _('Sekunden'), -1, (205, 136, 35, 8), 1342177280]], 'IDD_WIZARD_TRAINING_IS_IMPORTANT': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('SpamBayes kann nicht effektiv arbeiten, wenn es untrainiert ist.'), -1, (11, 8, 263, 11), 1342177280], [128, _('Training...'), 1017, (225, 140, 49, 15), 1342177280], [130, _("SpamBayes besitzt keine vordefinierten Regeln sondern lernt von Ihnen, Spam von 'guten' Nachrichten (Ham) zu unterscheiden. Sie m\xfcssen SpamBayes deshalb Ordner mit guten und schlechten Nachrichten zum Training zur Verf\xfcgung stellen."), -1, (11, 21, 263, 30), 1342177280], [130, _("In diesem Fall stellt SpamBayes anfangs alle Nachrichten in den Ordner 'unsicher'. W\xe4hrend Sie dann mit den Kn\xf6pfen 'Spam' und 'Kein Spam' die Nachrichten zuordnen, lernt SpamBayes den Umgang mit Ihren Nachrichten."), -1, (22, 61, 252, 29), 1342177280], [130, _('Diese Option wird den Assistenten beenden und erkl\xe4ren, wie Sie Ihre Nachrichten vorsortieren k\xf6nnen. Danach k\xf6nnen Sie SpamBayes trainieren und SpamBayes wird sofort beginnen, effektiv zu arbeiten.'), -1, (22, 106, 252, 27), 1342177280], [130, _("F\xfcr mehr Informationen bet\xe4tigen Sie bitte den Knopf 'Training...'"), -1, (11, 143, 211, 12), 1342177280], [128, _('Ich m\xf6chte ohne Training fortfahren'), 1088, (11, 50, 263, 11), 1342308361], [128, _('Ich werde die Nachrichten vorsortieren und SpamBayes danach konfigurieren.'), 1089, (11, 92, 263, 11), 1342177289]], 'IDD_FOLDER_SELECTOR': [[_('Ordner ausw\xe4hlen'), (0, 0, 247, 215), -1865940800, None, (8, 'Tahoma')], [130, _('&Folders:'), -1, (7, 7, 47, 9), 1342177280], ['SysTreeView32', '', 1040, (7, 21, 172, 140), 1350631735], [128, _('(sub)'), 1041, (7, 167, 126, 9), 1342242819], [130, _('(status1)'), 1043, (7, 180, 220, 9), 1342177280], [130, _('(status2)'), 1044, (7, 194, 220, 9), 1342177280], [128, _('OK'), 1, (190, 21, 50, 14), 1342177281], [128, _('Abbrechen'), 2, (190, 39, 50, 14), 1342177280], [128, _('Alle l\xf6schen'), 1042, (190, 58, 50, 14), 1342177280], [128, _('Neuer Ordner'), 1046, (190, 77, 50, 14), 1342177280]], 'IDD_WIZARD_FOLDERS_WATCH': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [128, _('Durchsuchen'), 1039, (225, 134, 50, 14), 1342177280], [130, _('Ordner, in denen neue Nachrichten eintreffen'), -1, (20, 4, 247, 14), 1342177280], [130, _('SpamBayes muss wissen, in welchen Ordnern neue Nachrichten eintreffen. In den meisen F\xe4llen ist dies der Posteingang. Sie k\xf6nnen aber weitere Ordner angeben, die von SpamBayes \xfcberwacht werden sollen.'), -1, (20, 21, 247, 25), 1342177280], [130, _("Die folgende Liste enth\xe4lt die zu beobachtenden Ordner. Dr\xfccken Sie auf 'Durchsuchen', um die Liste zu \xe4ndern, bzw. auf 'Weiter', um fortzufahren."), -1, (20, 79, 247, 20), 1342177280], [130, _('Wenn Sie den Outlook Regelassistenten benutzen, um Nachrichten zu verschieben, k\xf6nnen Sie solche Ordner zus\xe4tzlich angeben.'), -1, (20, 51, 241, 20), 1342177280], [129, '', 1038, (20, 100, 195, 48), 1350568068]], 'IDD_WIZARD_FINISHED_TRAINED': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [130, _('Gratulation'), -1, (20, 4, 247, 14), 1342177280], [130, _('SpamBayes wurde erfolgreich trainiert und konfiguriert. SpamBayes sollte jetzt bereit sein, die Nachrichten effektiv zu filtern.'), 1035, (20, 35, 247, 26), 1342177280], [130, _("Obwohl SpamBayes jetzt erfolgreich trainiert wurde, lernt SpamBayes weiter. Bitte schauen Sie regelm\xe4\xdfig in den Ordner mit den 'unsicheren' Nachrichten und benutzen die Schaltfl\xe4chen 'Spam' und 'Kein Spam'."), -1, (20, 68, 249, 30), 1342177280], [130, _('Klicken Sie auf Beenden, um den Assistenten zu schlie\xdfen.'), -1, (20, 104, 257, 23), 1342177280]], 'IDD_STATISTICS': [[_('Statistik'), (0, 0, 248, 257), 1354760256, None, (8, 'Tahoma')], [128, _('Statistik'), -1, (7, 3, 241, 229), 1342177287], [130, _('some stats\\nand some more\\nline 3\\nline 4\\nline 5'), 1095, (12, 12, 230, 204), 1342177280], [128, _('Statistik zur\xfccksetzen'), 1096, (165, 238, 83, 14), 1342177280], [130, _('Zuletzt zur\xfcckgesetzt:'), -1, (7, 241, 72, 8), 1342177280], [130, _('<<<Date>>>'), 1097, (84, 241, 70, 8), 1342177280]], 'IDD_WIZARD_FOLDERS_TRAIN': [['', (0, 0, 284, 162), 1354760256, 1024, (8, 'Tahoma')], [128, _('Druchsuchen'), 1004, (208, 49, 60, 15), 1342177280], [130, _('Training'), -1, (20, 4, 247, 10), 1342177280], [130, _("Bitte w\xe4hlen Sie die Nachrichten mit dem vorsortierten Spam und den vorsortierten 'guten' Nachrichten."), -1, (20, 16, 243, 16), 1342177280], [129, '', 1083, (20, 49, 179, 14), 1350568064], [130, _('Beispiele von Spam und anderer unerw\xfcnschter Nachrichten finden sich hier:'), -1, (20, 71, 248, 8), 1342177280], [129, '', 1027, (20, 81, 177, 14), 1350568064], [130, _("Beispiele 'guter' Nachrichten finden sich unter"), -1, (20, 38, 153, 8), 1342177280], [128, _('Durchsuchen'), 1005, (208, 81, 60, 15), 1342177280], [130, _('Wenn Sie keine vorsortierten Nachrichten haben oder bereits vorhandene SpamBayes-Daten weiter benutzen m\xf6chten, gehen Sie bitte zur\xfcck und geben an, dass Sie sich nicht vorbereitet haben.'), -1, (20, 128, 243, 26), 1342177280], [128, _('Nachrichten nach dem Training bewerten'), 1008, (20, 108, 163, 16), 1342242819]], 'IDD_ADVANCED': [[_('Erweitert'), (0, 0, 248, 257), 1354760256, 1024, (8, 'Tahoma')], [128, _('Zeitliches Verhalten'), -1, (7, 3, 234, 117), 1342177287], ['msctls_trackbar32', '', 1056, (16, 36, 148, 22), 1342242821], [130, _('Wartezeit vor dem start'), -1, (16, 26, 101, 8), 1342177280], [129, '', 1057, (165, 39, 40, 14), 1350566016], [130, _('seconds'), -1, (208, 41, 28, 8), 1342177280], ['msctls_trackbar32', '', 1058, (16, 73, 148, 22), 1342242821], [130, _('Wartezeit zwischen zwei Elementen'), -1, (16, 62, 142, 8), 1342177280], [129, '', 1059, (165, 79, 40, 14), 1350566016], [130, _('seconds'), -1, (207, 82, 28, 8), 1342177280], [128, _('Nur f\xfcr Ordner, die neue Nachrichten erhalten'), 1060, (16, 100, 217, 10), 1342242819], [128, _('Datenordner zeigen'), 1071, (7, 238, 70, 14), 1342177280], [128, _('Filtern im Hintergrund aktivieren'), 1091, (16, 12, 162, 10), 1342242819], [128, _('Diagnose...'), 1080, (171, 238, 70, 14), 1342177280]]}
    ids = {'IDC_DELAY1_SLIDER': 1056, 'IDC_PROGRESS': 1000, 'IDD_MANAGER': 101, 'IDD_DIAGNOSTIC': 113, 'IDD_TRAINING': 102, 'IDC_DELAY2_TEXT': 1059, 'IDC_DELAY1_TEXT': 1057, 'IDD_WIZARD': 114, 'IDC_BROWSE_SPAM_SOUND': 1103, 'IDC_STATIC_HAM': 1002, 'IDC_PROGRESS_TEXT': 1001, 'IDD_GENERAL': 108, 'IDC_BROWSE_UNSURE_SOUND': 1102, 'IDC_TAB': 1068, 'IDC_FOLDER_UNSURE': 1033, 'IDC_VERBOSE_LOG': 1061, 'IDC_EDIT1': 1094, 'IDC_BROWSE': 1037, 'IDC_BACK_BTN': 1069, 'IDD_WIZARD_FINISHED_UNCONFIGURED': 119, 'IDC_ACTION_CERTAIN': 1025, 'IDC_BUT_ACT_ALL': 1019, 'IDD_FILTER_NOW': 104, 'IDC_BROWSE_HAM_SOUND': 1101, 'IDC_MARK_SPAM_AS_READ': 1047, 'IDC_RECOVER_RS': 1075, 'IDC_STATIC': -1, 'IDC_PAGE_PLACEHOLDER': 1078, 'IDC_BROWSE_WATCH': 1039, 'IDC_ACCUMULATE_DELAY_TEXT': 1100, 'IDC_FOLDER_HAM': 1083, 'IDD_WIZARD_FOLDERS_REST': 117, 'IDC_SHOW_DATA_FOLDER': 1071, 'IDC_BUT_ACT_SCORE': 1018, '_APS_NEXT_RESOURCE_VALUE': 129, '_APS_NEXT_SYMED_VALUE': 101, 'IDC_SLIDER_CERTAIN': 1023, 'IDC_BUT_UNREAD': 1020, 'IDC_BUT_ABOUT': 1017, 'IDC_BUT_RESCORE': 1008, 'IDC_BUT_SEARCHSUB': 1041, 'IDC_BUT_TRAIN_FROM_SPAM_FOLDER': 1010, 'IDC_LAST_RESET_DATE': 1097, 'IDD_WIZARD_FOLDERS_TRAIN': 120, 'IDC_BUT_FILTER_ENABLE': 1013, 'IDC_ABOUT_BTN': 1072, 'IDD_WIZARD_FINISHED_TRAINED': 122, 'IDD_FOLDER_SELECTOR': 105, 'IDD_STATISTICS': 107, 'IDC_LIST_FOLDERS': 1040, 'IDB_SBWIZLOGO': 125, 'IDC_BUT_VIEW_LOG': 1093, 'IDC_STATUS2': 1044, 'IDC_STATUS1': 1043, 'IDCANCEL': 2, 'IDC_BROWSE_HAM': 1004, 'IDC_BROWSE_SPAM': 1005, 'IDD_WIZARD_FINISHED_UNTRAINED': 116, 'IDC_MARK_UNSURE_AS_READ': 1051, 'IDC_BROWSE_HAM_SOUND2': 1103, 'IDC_BUT_WIZARD': 1070, 'IDC_VERSION': 1009, 'IDC_FOLDER_NAMES': 1036, 'IDC_BUT_TIMER_ENABLED': 1091, 'IDC_SLIDER_UNSURE': 1029, 'IDC_BUT_NEW': 1046, 'IDC_FOLDER_WATCH': 1038, 'IDC_BUT_UNTRAINED': 1088, 'IDC_STATIC_SPAM': 1003, 'IDC_EDIT_UNSURE': 1030, 'IDC_BUT_CLEARALL': 1042, 'IDC_BUT_UNSEEN': 1021, 'IDD_WIZARD_FOLDERS_WATCH': 118, 'IDC_HAM_SOUND': 1094, 'IDC_EDIT_CERTAIN': 1024, 'IDC_BUT_FILTER_DEFINE': 1016, 'IDC_FORWARD_BTN': 1077, '_APS_NEXT_CONTROL_VALUE': 1102, 'IDC_INBOX_TIMER_ONLY': 1060, 'IDD_ADVANCED': 106, 'IDC_WIZ_GRAPHIC': 1092, 'IDC_DEL_SPAM_RS': 1074, 'IDB_FOLDERS': 127, 'IDC_BUT_PREPARATION': 1081, 'IDC_DELAY2_SLIDER': 1058, 'IDC_ACCUMULATE_DELAY_SLIDER': 1099, 'IDC_SAVE_SPAM_SCORE': 1048, 'IDC_FOLDER_CERTAIN': 1027, 'IDB_SBLOGO': 1062, 'IDC_BROWSE_UNSURE': 1034, 'IDC_STATISTICS': 1095, 'IDC_BUT_RESET_STATS': 1096, 'IDC_BUT_TRAIN_TO_SPAM_FOLDER': 1011, 'IDD_FILTER_SPAM': 110, 'IDC_BUT_RESET': 1073, 'IDD_NOTIFICATIONS': 128, 'IDC_ACTION_UNSURE': 1031, 'IDD_WIZARD_TRAIN': 121, 'IDD_WIZARD_FINISHED_TRAIN_LATER': 124, 'IDC_ACTION_HAM': 1032, 'IDC_BUT_REBUILD': 1007, '_APS_NEXT_COMMAND_VALUE': 40001, 'IDC_ENABLE_SOUNDS': 1098, 'IDC_SPAM_SOUND': 1096, 'IDC_UNSURE_SOUND': 1095, 'IDD_WIZARD_TRAINING_IS_IMPORTANT': 123, 'IDC_TRAINING_STATUS': 1035, 'IDD_WIZARD_WELCOME': 115, 'IDC_BUT_TRAIN': 1089, 'IDC_START': 1006, 'IDD_FILTER': 103, 'IDC_LOGO_GRAPHIC': 1063, 'IDC_FILTER_STATUS': 1014, 'IDOK': 1, 'IDC_BROWSE_CERTAIN': 1028, 'IDC_BUT_SHOW_DIAGNOSTICS': 1080, 'IDC_BUT_TRAIN_NOW': 1012}
    names = {1024: 'IDC_EDIT_CERTAIN', 1: 'IDOK', 2: 'IDCANCEL', 1027: 'IDC_FOLDER_CERTAIN', 1028: 'IDC_BROWSE_CERTAIN', 1029: 'IDC_SLIDER_UNSURE', 1030: 'IDC_EDIT_UNSURE', 1031: 'IDC_ACTION_UNSURE', 1032: 'IDC_ACTION_HAM', 1033: 'IDC_FOLDER_UNSURE', 1034: 'IDC_BROWSE_UNSURE', 1035: 'IDC_TRAINING_STATUS', 1036: 'IDC_FOLDER_NAMES', 1037: 'IDC_BROWSE', 1038: 'IDC_FOLDER_WATCH', 1039: 'IDC_BROWSE_WATCH', 1040: 'IDC_LIST_FOLDERS', 1041: 'IDC_BUT_SEARCHSUB', 1042: 'IDC_BUT_CLEARALL', 1043: 'IDC_STATUS1', 1044: 'IDC_STATUS2', 1046: 'IDC_BUT_NEW', 1047: 'IDC_MARK_SPAM_AS_READ', 1048: 'IDC_SAVE_SPAM_SCORE', 1051: 'IDC_MARK_UNSURE_AS_READ', 1056: 'IDC_DELAY1_SLIDER', 1057: 'IDC_DELAY1_TEXT', 1058: 'IDC_DELAY2_SLIDER', 1059: 'IDC_DELAY2_TEXT', 1060: 'IDC_INBOX_TIMER_ONLY', 1061: 'IDC_VERBOSE_LOG', 1062: 'IDB_SBLOGO', 1063: 'IDC_LOGO_GRAPHIC', 1068: 'IDC_TAB', 1069: 'IDC_BACK_BTN', 1070: 'IDC_BUT_WIZARD', 1071: 'IDC_SHOW_DATA_FOLDER', 1072: 'IDC_ABOUT_BTN', 1073: 'IDC_BUT_RESET', 1074: 'IDC_DEL_SPAM_RS', 1075: 'IDC_RECOVER_RS', 1077: 'IDC_FORWARD_BTN', 1078: 'IDC_PAGE_PLACEHOLDER', 1080: 'IDC_BUT_SHOW_DIAGNOSTICS', 1081: 'IDC_BUT_PREPARATION', 1083: 'IDC_FOLDER_HAM', 1088: 'IDC_BUT_UNTRAINED', 1089: 'IDC_BUT_TRAIN', 1091: 'IDC_BUT_TIMER_ENABLED', 1025: 'IDC_ACTION_CERTAIN', 1093: 'IDC_BUT_VIEW_LOG', 1094: 'IDC_EDIT1', 1095: 'IDC_STATISTICS', 1096: 'IDC_BUT_RESET_STATS', 1097: 'IDC_LAST_RESET_DATE', 1098: 'IDC_ENABLE_SOUNDS', 1099: 'IDC_ACCUMULATE_DELAY_SLIDER', 1100: 'IDC_ACCUMULATE_DELAY_TEXT', 1101: 'IDC_BROWSE_HAM_SOUND', 1102: 'IDC_BROWSE_UNSURE_SOUND', 1103: 'IDC_BROWSE_HAM_SOUND2', 101: 'IDD_MANAGER', 102: 'IDD_TRAINING', 103: 'IDD_FILTER', 104: 'IDD_FILTER_NOW', 105: 'IDD_FOLDER_SELECTOR', 106: 'IDD_ADVANCED', 107: 'IDD_STATISTICS', 108: 'IDD_GENERAL', 110: 'IDD_FILTER_SPAM', 113: 'IDD_DIAGNOSTIC', 114: 'IDD_WIZARD', 115: 'IDD_WIZARD_WELCOME', 116: 'IDD_WIZARD_FINISHED_UNTRAINED', 117: 'IDD_WIZARD_FOLDERS_REST', 118: 'IDD_WIZARD_FOLDERS_WATCH', 119: 'IDD_WIZARD_FINISHED_UNCONFIGURED', 120: 'IDD_WIZARD_FOLDERS_TRAIN', 121: 'IDD_WIZARD_TRAIN', 122: 'IDD_WIZARD_FINISHED_TRAINED', 123: 'IDD_WIZARD_TRAINING_IS_IMPORTANT', 124: 'IDD_WIZARD_FINISHED_TRAIN_LATER', 125: 'IDB_SBWIZLOGO', 127: 'IDB_FOLDERS', 128: 'IDD_NOTIFICATIONS', 129: '_APS_NEXT_RESOURCE_VALUE', 40001: '_APS_NEXT_COMMAND_VALUE', 1092: 'IDC_WIZ_GRAPHIC', 1000: 'IDC_PROGRESS', 1001: 'IDC_PROGRESS_TEXT', 1002: 'IDC_STATIC_HAM', 1003: 'IDC_STATIC_SPAM', 1004: 'IDC_BROWSE_HAM', 1005: 'IDC_BROWSE_SPAM', 1006: 'IDC_START', 1007: 'IDC_BUT_REBUILD', 1008: 'IDC_BUT_RESCORE', 1009: 'IDC_VERSION', 1010: 'IDC_BUT_TRAIN_FROM_SPAM_FOLDER', 1011: 'IDC_BUT_TRAIN_TO_SPAM_FOLDER', 1012: 'IDC_BUT_TRAIN_NOW', 1013: 'IDC_BUT_FILTER_ENABLE', 1014: 'IDC_FILTER_STATUS', 1016: 'IDC_BUT_FILTER_DEFINE', 1017: 'IDC_BUT_ABOUT', 1018: 'IDC_BUT_ACT_SCORE', 1019: 'IDC_BUT_ACT_ALL', 1020: 'IDC_BUT_UNREAD', 1021: 'IDC_BUT_UNSEEN', -1: 'IDC_STATIC', 1023: 'IDC_SLIDER_CERTAIN'}
    bitmaps = {'IDB_SBWIZLOGO': 'sbwizlogo.bmp', 'IDB_SBLOGO': 'sblogo.bmp', 'IDB_FOLDERS': 'folders.bmp'}
def ParseDialogs(s):
    return FakeParser()
