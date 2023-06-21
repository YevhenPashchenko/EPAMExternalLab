const functions = require('../functions');
const should = require('chai').should();

describe('SecondsToDate', () => {
  it('return value should be equal to expected value', () => {
    const args = [31536000, 0, 86400];
    const expected = ["01.06.2021", "01.06.2020", "02.06.2020"];
    for (let i = 0; i < expected.length; i++) {
      new Intl.DateTimeFormat('uk-UA').format(
          functions.secondsToDate(args[i])).should.equal(expected[i]);
    }
  });
});

describe('ToBase2Converter', () => {
  it('return value should be equal to expected value', () => {
    const args = [0, 1, 5, 10, 1025];
    const expected = ["0", "1", "101", "1010", "1"];
    for (let i = 0; i < expected.length; i++) {
      functions.toBase2Converter(args[i]).should.equal(expected[i]);
    }
  });
});

describe('SubstringOccurrencesCounter', () => {
  it('return value should be equal to expected value', () => {
    const args = [["a", "test it"], ["t", "test it"], ["T", "test it"],
      ["te", "test it"]];
    const expected = [0, 3, 3];
    for (let i = 0; i < expected.length; i++) {
      functions.substringOccurrencesCounter(...args[i]).should.equal(
          expected[i]);
    }
  });
});

describe('RepeatingLitters', () => {
  it('return value should be equal to expected value', () => {
    const args = ["Hello", "Hello world"];
    const expected = ["HHeelloo", "HHeello  wworrldd"];
    for (let i = 0; i < expected.length; i++) {
      functions.repeatingLitters(args[i]).should.equal(expected[i]);
    }
  });
});

describe('Redundant', () => {
  it('return value should be equal to expected value', () => {
    const args = ["apple", "pear", ""];
    for (let i = 0; i < args.length; i++) {
      functions.redundant(args[i])().should.equal(args[i]);
    }
  });
});

describe('TowerHanoi', () => {
  it('return value should be equal to expected value', () => {
    const args = [3, 5, 7];
    const expected = [7, 31, 127];
    for (let i = 0; i < expected.length; i++) {
      functions.towerHanoi(args[i]).should.equal(expected[i]);
    }
  });
});

describe('MatrixMultiplication', () => {
  it('return value should be equal to expected value', () => {
    const args = [
      [[[1, 2], [3, 4]], [[1, 2], [3, 4]]],
      [[[1, 2, 3], [4, 5, 6], [7, 8, 9]], [[1, 2, 3], [4, 5, 6], [7, 8, 9]]]
    ];
    const expected = [
      [[7, 10], [15, 22]],
      [[30, 36, 42], [66, 81, 96], [102, 126, 150]]
    ];
    for (let i = 0; i < expected.length; i++) {
      functions.matrixMultiplication(...args[i]).should.eql(expected[i]);
    }
  });
});

describe('Gather', () => {
  it('return value should be equal to expected value', () => {
    functions.gather("a")("b")("c").order(0)(1)(2).get().should.equal("abc");
    functions.gather("a")("b")("c").order(2)(1)(0).get().should.equal("cba");
    functions.gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(
        4).get().should.equal("hello!");
  });
});