import threading, thread, gobject
from logfile import log
class AsyncRunner(threading.Thread):
    '''
    A class that attempts to run the given method with the specified arguments
    without distrupting the normal flow of the application; all arguments passed
    are copied to prevent threading issues. The passed callback is called
    in the same thread as whatever that created the class.
    '''
    def __init__(self, method, callback, *args):
        self.callback = callback
        copied_args = []
        import copy
        for arg in args:
            copied_args.append(copy.deepcopy(arg))
        self.copied_args = copied_args
        copied_method = copy.deepcopy(method)
        self.copied_method = copied_method
        threading.Thread.__init__(self)
        self.setDaemon(True)
        self.done_event = threading.Event()
        self.cancel_event = threading.Event()
        self.error_flag = threading.Event()
        gobject.timeout_add(100, self.check)
        self.start()
    def cancel(self):
        self.cancel_event.set()
    def check(self):
        ''' Checks to see if the connection attempt is finished. '''
        if self.done_event.isSet():
            self.callback(self.result)
            if self.error_flag.isSet():
                self.error_flag.clear()
            return False
        return True
    def abort_if_needed(self):
        if self.cancel_event.isSet():
            log('aborting connection attempt')
            self.result = None            
            self.error_flag.set()
            self.done_event.set()
    def run(self):
        try:
            result = self.copied_method(self.abort_if_needed, *self.copied_args)
        except:
            log( 'error occurred in AsyncRunner, exiting thread...')
            self.error_flag.set()
            result = None
            raise
        finally:
            self.result = result
            self.done_event.set()
        log( '%s: AsyncRunner thread exited' % self.copied_method.func_name)
class AsyncError(Exception): pass        
class AsyncManager:
    def __init__(self):
        self.__busy = False
    def __deepcopy__(self, memo):
        return None
    def __copy__(self):
        return None
    def __can_start(self):
        return not self.__busy
    def __start(self, name):
        self.__busy = True
        self.__current_name = name
    def __stop(self):
        self.__busy = False
        self.__current_name = None
    def run(self, method, callback, *args, **kwargs):
        if not self.__can_start():
            raise AsyncError('Async process %s already running.' % 
                             self.__current_name)
        self.__start(kwargs.get('name'))
        def new_callback(result=None):
            self.__stop()
            callback(result)
        self.__async_runner = AsyncRunner(method, new_callback, *args)
    def stop(self, name):
        if self.__current_name == name:
            self.__async_runner.cancel()
