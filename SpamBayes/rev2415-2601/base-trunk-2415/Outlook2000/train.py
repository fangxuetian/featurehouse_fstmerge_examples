import traceback
from win32com.mapi import mapi
try:
    True, False
except NameError:
    True, False = 1, 0
def been_trained_as_ham(msg, cdata):
    if not cdata.message_db.has_key(msg.searchkey):
        return False
    return cdata.message_db[msg.searchkey]=='0'
def been_trained_as_spam(msg, cdata):
    if not cdata.message_db.has_key(msg.searchkey):
        return False
    return cdata.message_db[msg.searchkey]=='1'
def train_message(msg, is_spam, cdata):
    from spambayes.tokenizer import tokenize
    if not cdata.message_db.has_key(msg.searchkey):
        was_spam = None
    else:
        was_spam = cdata.message_db[msg.searchkey]=='1'
    if was_spam == is_spam:
        return False    # already correctly classified
    stream = msg.GetEmailPackageObject()
    if was_spam is not None:
        cdata.bayes.unlearn(tokenize(stream), was_spam)
    cdata.bayes.learn(tokenize(stream), is_spam)
    cdata.message_db[msg.searchkey] = ['0', '1'][is_spam]
    cdata.dirty = True
    return True
def untrain_message(msg, cdata):
    from spambayes.tokenizer import tokenize
    stream = msg.GetEmailPackageObject()
    if been_trained_as_spam(msg, cdata):
        assert not been_trained_as_ham(msg, cdata), "Can't have been both!"
        cdata.bayes.unlearn(tokenize(stream), True)
        del cdata.message_db[msg.searchkey]
        cdata.dirty = True
        return True
    if been_trained_as_ham(msg, cdata):
        assert not been_trained_as_spam(msg, cdata), "Can't have been both!"
        cdata.bayes.unlearn(tokenize(stream), False)
        del cdata.message_db[msg.searchkey]
        cdata.dirty = True
        return False
    return None
def train_folder(f, isspam, cdata, progress):
    num = num_added = 0
    for message in f.GetMessageGenerator():
        if progress.stop_requested():
            break
        progress.tick()
        try:
            if train_message(message, isspam, cdata):
                num_added += 1
        except:
            print "Error training message '%s'" % (message,)
            traceback.print_exc()
        num += 1
    print "Checked", num, "in folder", f.name, "-", num_added, "new entries found."
def real_trainer(classifier_data, config, message_store, progress):
    progress.set_status("Counting messages")
    num_msgs = 0
    for f in message_store.GetFolderGenerator(config.training.ham_folder_ids, config.training.ham_include_sub):
        num_msgs += f.count
    for f in message_store.GetFolderGenerator(config.training.spam_folder_ids, config.training.spam_include_sub):
        num_msgs += f.count
    progress.set_max_ticks(num_msgs+3)
    for f in message_store.GetFolderGenerator(config.training.ham_folder_ids, config.training.ham_include_sub):
        progress.set_status("Processing good folder '%s'" % (f.name,))
        train_folder(f, 0, classifier_data, progress)
        if progress.stop_requested():
            return
    for f in message_store.GetFolderGenerator(config.training.spam_folder_ids, config.training.spam_include_sub):
        progress.set_status("Processing spam folder '%s'" % (f.name,))
        train_folder(f, 1, classifier_data, progress)
        if progress.stop_requested():
            return
    progress.tick()
    if progress.stop_requested():
        return
    progress.set_max_ticks(1)
    progress.set_status("Writing the database...")
    classifier_data.Save()
def trainer(mgr, config, progress):
    rebuild = config.training.rebuild
    rescore = config.training.rescore
    if not config.training.ham_folder_ids or not config.training.spam_folder_ids:
        progress.error("You must specify at least one spam, and one good folder")
        return
    if rebuild:
        import os, manager
        bayes_base = os.path.join(mgr.data_directory, "$sbtemp$default_bayes_database")
        mdb_base = os.path.join(mgr.data_directory, "$sbtemp$default_message_database")
        ManagerClass = manager.GetStorageManagerClass()
        db_manager = ManagerClass(bayes_base, mdb_base)
        classifier_data = manager.ClassifierData(db_manager, mgr)
        classifier_data.InitNew()
    else:
        classifier_data = mgr.classifier_data
    if rescore:
        stages = ("Training", .3), ("Saving", .1), ("Scoring", .6)
    else:
        stages = ("Training", .9), ("Saving", .1)
    progress.set_stages(stages)
    real_trainer(classifier_data, config, mgr.message_store, progress)
    if progress.stop_requested():
        return
    if rebuild:
        assert mgr.classifier_data is not classifier_data
        mgr.classifier_data.Adopt(classifier_data)
        classifier_data = mgr.classifier_data
    progress.tick()
    if rescore:
        config = mgr.config.filter_now
        config.only_unread = False
        config.only_unseen = False
        config.action_all = False
        config.folder_ids = mgr.config.training.ham_folder_ids + mgr.config.training.spam_folder_ids
        config.include_sub = mgr.config.training.ham_include_sub or mgr.config.training.spam_include_sub
        import filter
        filter.filterer(mgr, mgr.config, progress)
    bayes = classifier_data.bayes
    progress.set_status("Completed training with %d spam and %d good messages" % (bayes.nspam, bayes.nham))
def main():
    print "Sorry - we don't do anything here any more"
if __name__ == "__main__":
    main()
