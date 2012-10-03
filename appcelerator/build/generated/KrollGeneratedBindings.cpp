/* C++ code produced by gperf version 3.0.3 */
/* Command-line: gperf -L C++ -E -t /private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf  */
/* Computed positions: -k'' */

#line 3 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"


#include <string.h>
#include <v8.h>
#include <KrollBindings.h>

#include "com.tapit.AlertAdProxy.h"
#include "com.tapit.TapitandroidModule.h"
#include "com.tapit.BannerViewProxy.h"
#include "com.tapit.InterstitialAdProxy.h"


#line 16 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"
struct titanium::bindings::BindEntry;
/* maximum key range = 8, duplicates = 0 */

class TapitandroidBindings
{
private:
  static inline unsigned int hash (const char *str, unsigned int len);
public:
  static struct titanium::bindings::BindEntry *lookupGeneratedInit (const char *str, unsigned int len);
};

inline /*ARGSUSED*/
unsigned int
TapitandroidBindings::hash (register const char *str, register unsigned int len)
{
  return len;
}

struct titanium::bindings::BindEntry *
TapitandroidBindings::lookupGeneratedInit (register const char *str, register unsigned int len)
{
  enum
    {
      TOTAL_KEYWORDS = 4,
      MIN_WORD_LENGTH = 22,
      MAX_WORD_LENGTH = 29,
      MIN_HASH_VALUE = 22,
      MAX_HASH_VALUE = 29
    };

  static struct titanium::bindings::BindEntry wordlist[] =
    {
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""},
      {""}, {""}, {""}, {""},
#line 18 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"
      {"com.tapit.AlertAdProxy", ::com::tapit::tapitandroid::AlertAdProxy::bindProxy, ::com::tapit::tapitandroid::AlertAdProxy::dispose},
      {""}, {""},
#line 20 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"
      {"com.tapit.BannerViewProxy", ::com::tapit::tapitandroid::BannerViewProxy::bindProxy, ::com::tapit::tapitandroid::BannerViewProxy::dispose},
      {""}, {""},
#line 19 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"
      {"com.tapit.TapitandroidModule", ::com::tapit::TapitandroidModule::bindProxy, ::com::tapit::TapitandroidModule::dispose},
#line 21 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"
      {"com.tapit.InterstitialAdProxy", ::com::tapit::tapitandroid::InterstitialAdProxy::bindProxy, ::com::tapit::tapitandroid::InterstitialAdProxy::dispose}
    };

  if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH)
    {
      register int key = hash (str, len);

      if (key <= MAX_HASH_VALUE && key >= 0)
        {
          register const char *s = wordlist[key].name;

          if (*str == *s && !strcmp (str + 1, s + 1))
            return &wordlist[key];
        }
    }
  return 0;
}
#line 22 "/private/var/folders/nz/5_tx8gn90pj9tqzhcwsx5gpr0000gn/T/retina/tapitandroid-generated/KrollGeneratedBindings.gperf"

