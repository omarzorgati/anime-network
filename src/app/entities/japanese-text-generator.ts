export class JapaneseTextGenerator {
  private static readonly japaneseCharacters = [
    'あ', 'い', 'う', 'え', 'お',
    'か', 'き', 'く', 'け', 'こ',
    'さ', 'し', 'す', 'せ', 'そ',
  ];

  generateRandomJapaneseText(length: number): string {
    let randomJapaneseText = '';
    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * JapaneseTextGenerator.japaneseCharacters.length);
      randomJapaneseText += JapaneseTextGenerator.japaneseCharacters[randomIndex];
    }
    return randomJapaneseText;
  }
}
