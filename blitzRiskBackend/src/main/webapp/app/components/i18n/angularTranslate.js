
var angularTranslate = angular.module('angularTranslate', ['pascalprecht.translate']);

angularTranslate.config(function ($translateProvider) {
    $translateProvider.translations('en', {
        HOME: 'BlitzRisk',
        PROFILE: 'Profile',
        OVERVIEW: 'Overview',

        LOGIN: 'Log in',
        SIGNUP: 'Sign up',
        LOGOUT: ' Log out',

        LOGINPAGE_LOGIN: 'Login',
        LOGINPAGE_BTN: 'login',
        LOGINPAGE_NOACCT: "Don't have an account?",
        LOGINPAGE_REG: 'Please, Register',
        LOGINPAGE_WRONGCRED: 'Wrong credentials',
        LOGINPAGE_LOGGING_IN: 'Logging in..',

        REGISTER_PAGE_REGISTER: 'Register',
        REGISTER_PAGE_BTN: 'Register',
        REGISTER_PAGE_HAVE_ACCT: 'Already have an account?',
        REGISTER_PAGE_PLEASE_LOGIN: 'Please, log in.',
        REGISTER_PAGE_BUSY: 'Registering..',
        REGISTER_PAGE_REGISTERED: 'You have been registered! Please',
        REGISTER_PAGE_LOGIN: 'Log In',
        REGISTER_PAGE_USR_EXISTS: 'E-mail address already in use',

        OVERVIEW_FRIENDS: 'Friends',
        OVERVIEW_ADD_FRIEND: 'Add',
        OVERVIEW_GAMES: 'Games',
        OVERVIEW_GAMENO: 'Game: ',
        OVERVIEW_CREATE_GAME: 'Create new Game',
        OVERVIEW_ADD_RANDOM: 'Add Random',
        OVERVIEW_WAITING: 'waiting for players',
        OVERVIEW_PLAY: 'Play',
        OVERVIEW_ACCEPT: 'Accept',
        OVERVIEW_PENDING: 'Pending',
        OVERVIEW_ACCEPTED: 'Accepted',

        CHATBOX_SEND: 'Send',

        PROFILE_EDIT_PROFILE: 'Edit Profile',
        PROFILE_PERSONAL_INFO: 'Personal info',
        PROFILE_USERNAME: 'Username:',
        PROFILE_EMAIL: 'E-mail:',
        PROFILE_PWD: 'Password:',
        PROFILE_NEW_PWD: 'New password:',
        PROFILE_CFRM_NEW_PWD: 'Confirm new password:',
        PROFILE_CANCEL: 'Cancel',
        PROFILE_SAVECHANGES: 'Save Changes',
        PROFILE_SAVED: 'Saved'

    });
    $translateProvider.translations('nl', {
        HOME: 'BlitzRisk',
        PROFILE: 'Profiel',
        OVERVIEW: 'Overzicht',

        LOGIN: 'Inloggen',
        SIGNUP: 'Registreren',
        LOGOUT: ' Uitloggen',

        LOGINPAGE_LOGIN: 'Inloggen',
        LOGINPAGE_BTN: 'inloggen',
        LOGINPAGE_NOACCT: "Heeft u geen account?",
        LOGINPAGE_REG: 'Gelieve u te registreren',
        LOGINPAGE_WRONGCRED: 'Verkeerde logingegevens',
        LOGINPAGE_LOGGING_IN: 'Bezig met inloggen..',

        REGISTER_PAGE_REGISTER: 'Registreren',
        REGISTER_PAGE_BTN: 'Registreren',
        REGISTER_PAGE_HAVE_ACCT: 'Heeft u reeds een account?',
        REGISTER_PAGE_PLEASE_LOGIN: 'Gelieve in te loggen.',
        REGISTER_PAGE_BUSY: 'Bezig met registreren..',
        REGISTER_PAGE_REGISTERED: 'U heeft zich geregistreerd! Gelieve',
        REGISTER_PAGE_LOGIN: 'in te loggen',
        REGISTER_PAGE_USR_EXISTS: 'E-mail adres is reeds in gebruik',

        OVERVIEW_FRIENDS: 'Vrienden',
        OVERVIEW_ADD_FRIEND: 'Voeg speler toe',
        OVERVIEW_GAMES: 'Spellen',
        OVERVIEW_GAMENO: 'Spel: ',
        OVERVIEW_CREATE_GAME: 'Creëer nieuw spel',
        OVERVIEW_ADD_RANDOM: 'Voeg willekeurige speler toe',
        OVERVIEW_WAITING: 'wachten op spelers',
        OVERVIEW_PLAY: 'Start',
        OVERVIEW_ACCEPT: 'Accepteren',
        OVERVIEW_PENDING: 'Wachtend',
        OVERVIEW_ACCEPTED: 'Geaccepteerd',

        CHATBOX_SEND: 'Verzenden',

        PROFILE_EDIT_PROFILE: 'Profiel bewerken',
        PROFILE_PERSONAL_INFO: 'Persoonlijke gegevens',
        PROFILE_USERNAME: 'Gebruikersnaam:',
        PROFILE_EMAIL: 'E-mail:',
        PROFILE_PWD: 'Wachtwoord:',
        PROFILE_NEW_PWD: 'Nieuw wachtwoord:',
        PROFILE_CFRM_NEW_PWD: 'Bevestig nieuw wachtwoord:',
        PROFILE_CANCEL: 'Annuleren',
        PROFILE_SAVECHANGES: 'Veranderingen opslaan',
        PROFILE_SAVED: 'Opgeslagen'
    });
    $translateProvider.preferredLanguage('en');
});

angularTranslate.controller('AngularTranslateController', function ($scope, $translate) {
    $scope.changeLanguage = function (key) {
        $translate.use(key);
    };
});
